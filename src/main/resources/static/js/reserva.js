

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
        if (res.status == 410) {            
            const mensaje = document.getElementById("mensajeerror");
            todapantallaerror.style.display="flex";
            mensaje.style.display = "block";
            const textomensaje = document.getElementById("textomensajeerror");
            textomensaje.textContent = "Este libro ya esta reservado";
            document.getElementById("titulomensajeerror").textContent = "ERROR";
        }
        else {
            res.json()
            .then( data => {            
                console.log(data)
                //pedir a BD los nuevos datos, introducirlos en sesión local y mostrar
                localStorage.setItem("reserva", JSON.stringify(data));
                
                mostrarReservas();
            }) 
        }      
    }).catch(err => { console.log(err)})
}

function mostrarReservas () {
     
    const carrito = JSON.parse(localStorage.getItem("reserva"));
    const bodytablaReserva = document.getElementById("tablareserva");
    const cabeceratablaReserva = document.getElementById ("cabeceratablareserva");
    bodytablaReserva.innerHTML = '';
    cabeceratablaReserva.innerHTML = '';
    
    if (carrito) {
        todapantallapopup2.style.display="block";   
        popup2.style.display="block";    
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
                            //Borrar la reserva de la sesión local         
                            localStorage.removeItem("reserva");                            
                            localStorage.setItem("reserva", JSON.stringify(data));
                            mostrarReservas();
                        })
                    })
                    .catch( err => {
                            const mensaje = document.getElementById("mensajeerror");
                            todapantallaerror.style.display="flex";
                            mensaje.style.display = "block";
                            const textomensaje = document.getElementById("textomensajeerror");
                            textomensaje.textContent = "Fallo algo.No se puede borrar";
                            document.getElementById("titulomensajeerror").textContent = "ERROR";
                    })
                })
            } 
        }

        
    }
    let volverCatalogo=crearElementoTexto('button',popup2,"Ver Catálogo");
    volverCatalogo.className="btnVolverCat";
    volverCatalogo.addEventListener('click',function(){ 
        todapantallapopup2.style.display="none";       
    })
} 

