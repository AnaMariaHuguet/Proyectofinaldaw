var todapantallaerror, cerrarError, cerrarReserva,popupcontenedor2;

window.addEventListener('load',function(){
    let mostrarMenu=document.getElementById("mostrarMenu");
    let ocultarMenu=document.getElementById("ocultarMenu");
    var navMenu=document.getElementById("navMenu");
    const carrito=document.getElementById("carrito");
    popupcontenedor2=document.getElementById("popupcontenedor2"); 
    let popup2 = document.getElementById("popup2"); 
    cerrarReserva=document.getElementById("cerrarReserva");
    cerrarError=document.getElementById("cerrarError");
    todapantallaerror=document.getElementById("errorcontenedor"); 
    nombreusuario=document.getElementById("nombreUsuario").value;

    mostrarMenu.addEventListener("click", function(){
        navMenu.style.display="flex";
    });
    ocultarMenu.addEventListener("click", function(){
        navMenu.style.display="none";
    });

    window.addEventListener('resize', function () {
        if (window.innerWidth > 800) {
            navMenu.style.display="flex";
        }
        else {
            navMenu.style.display="none";            
        }
    });
//poner que si es visitante pase de esto
    if(nombreusuario!="Visitante"){
    carrito.addEventListener('click', getReservas);  
    }
    popupcontenedor2.addEventListener("click", function(){       
        this.style.display="none";
    });

    popup2.addEventListener("click", function (e) {
        e.stopPropagation();
    })
    cerrarReserva.addEventListener("click", function(){
        popupcontenedor2.style.display="none";

    });
    cerrarError.addEventListener("click", function(){
        todapantallaerror.style.display="none";
     });
    
})

function crearElementoTexto( tipo="div", padre=contenido, texto=""){
    let elemento=document.createElement(tipo);
    if (texto != "")
        elemento.textContent=texto;
    padre.appendChild(elemento);
    return elemento;
}

function realizaReserva( e ){
    e.preventDefault ();  
    const idLibro = e.target.value;
   
    fetch('/api/reserva',{
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify({id : idLibro}) 
    }).then( res => {        
        res.json()
        .then( data => { 
            if (data.status > 400 ){                
                salidaMensajeError(data.message);
            }                          
            else {           
                mostrarReservas(data);
            }
        }) 
              
    }).catch(err => { console.log(err)})
}

function mostrarReservas (carrito) {
    const bodytablaReserva = document.getElementById("tablareserva");
    const cabeceratablaReserva = document.getElementById ("cabeceratablareserva");
    bodytablaReserva.innerHTML = '';
    cabeceratablaReserva.innerHTML = '';
    const popupcontenedor2 = document.getElementById ("popupcontenedor2");

    if (carrito) {
        popupcontenedor2.style.display="block";   
        let filaTitulo = crearElementoTexto('tr',cabeceratablaReserva);
        crearElementoTexto('th',filaTitulo,"Id Libro");       
        crearElementoTexto('th',filaTitulo,"Título"); 
        crearElementoTexto('th',filaTitulo,"Eliminar");        

        for(let i=0; i<carrito.length; i++){
            let fila=crearElementoTexto('tr',bodytablaReserva); 
           
            if(carrito[i].libro) {
              crearElementoTexto('td',fila,carrito[i].libro.id);             
                crearElementoTexto('td',fila,carrito[i].libro.titulo); 
                let celdaBorrar=crearElementoTexto('td',fila);
                let borrarReserva=crearElementoTexto('button',celdaBorrar,"Borrar");
                borrarReserva.value =carrito[i].libro.id;                
                borrarReserva.name = 'borrar';
                borrarReserva.className="btnDetRes";
                let icon = crearElementoTexto('i', borrarReserva);
                icon.setAttribute("class","fas fa-trash-alt margin-5"); 
                celdaBorrar.addEventListener('click',function(){
                    // Borrar reserva de la Bd reserva
                    fetch('/api/reserva/'+carrito[i].libro.id,{
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        method: "DELETE",                        
                    })
                    .then( res => {
                        res.json()
                        .then( data => {                              
                            mostrarReservas(data);
                        })
                    })
                    .catch( err => {
                            console.log(err.response);
                           let mensaje = "Fallo algo.No se puede borrar";
                           salidaMensajeError(mensaje);
                    })
                })
            } 
        }
        let irReserva=crearElementoTexto('button',popup2,"ir a Reserva");
        irReserva.className="btnirReserva"; 
        irReserva.addEventListener('click',function(){ 
            //redireccionar a otra ventana con javascript
            window.location.href = "/usuario/reserva";
        })
    }   
} 

function getReservas(){    
    fetch('/api/reserva/usuario',{
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST",        
    }).then( res => {        
        res.json()
        .then( data => { 
            if (data.status > 400 ){                
                salidaMensajeError(data.message);                
            }                          
            else { 
                mostrarReservas(data);
            }
        })               
    }).catch(err => { console.log(err)})
}
function salidaMensajeError(message, titulo="ERROR"){
    const mensaje = document.getElementById("mensajeerror");
    todapantallaerror.style.display="flex";
    mensaje.style.display = "block";
    const textomensaje = document.getElementById("textomensajeerror");
    textomensaje.textContent = message;
    document.getElementById("titulomensajeerror").textContent = titulo;
}