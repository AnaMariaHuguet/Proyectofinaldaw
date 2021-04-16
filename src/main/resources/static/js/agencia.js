
var peticion, datosjson, opcion, url, selgenero, selcategoria, selautor, contenido,bodytabla;
var todapantallapopup, todapantallaerror, todapantallapopup2;

function crearElementoTexto( tipo="div", padre=contenido, texto=""){
    let elemento=document.createElement(tipo);
    if (texto != "")
        elemento.textContent=texto;
    padre.appendChild(elemento);
    return elemento;
}

function procesaRespuesta(){    
    if(peticion.status===200){
        datosjson=JSON.parse(peticion.responseText);
       // if (datosjson!=null){
            mostrarLibros(datosjson); 
       // }else{
           /* switch(opcion){
                case 'genero':
                    let fila=crearElementoTexto('tr',bodytabla);
                    crearElementoTexto('td',fila, "No disponemos libros de ese genero");
                break;
                case 'autor': 
                let fila1=crearElementoTexto('tr',bodytabla);
                crearElementoTexto('td',fila1, "No disponemos libros de ese autor");
                break;
                case 'categoria':*/
                 /*   const mensaje1 = document.getElementById("mensajeerror");
    todapantallaerror.style.display="flex";
    mensaje1.style.display = "block";
    const textomensaje1 = document.getElementById("textomensajeerror");
    textomensaje1.textContent = "No disponemos libros de esa categoria";
    document.getElementById("titulomensajeerror").textContent = "ERROR";
                              
           // } */
       // }
                  
    }
}
function realizaPeticionAjax(opc,param){
    opcion=opc;
    peticion=new XMLHttpRequest();
    url="/api/libro";
    let params="";     
    switch(opcion){
        case 'genero':params = JSON.stringify({'genero': param});
            url += "/genero";
        break;
        case 'autor':params = JSON.stringify({'autor': param});
            url += "/autor";        
        break;
        case 'categoria':params = JSON.stringify({'categoria': param});
            url += "/categoria";
        break;                      
    }
    peticion.open('POST', url); 
    peticion.setRequestHeader("Content-Type", "application/json;charset=UTF-8");       
    peticion.addEventListener('load',procesaRespuesta);
    peticion.send(params);
}
function realizaPeticionCategorias(param){
    let peticion1=new XMLHttpRequest();
    url="/api/genero/id";
    const params=JSON.stringify({'genero': param});     
    peticion1.open('POST', url); 
    peticion1.setRequestHeader("Content-Type", "application/json;charset=UTF-8");       
    peticion1.addEventListener('load',() => procesaCategoriasRespuesta(peticion1));
    peticion1.send(params);
}
function procesaCategoriasRespuesta(peti){    
    if(peti.status===200){
        const datos=JSON.parse(peti.responseText);
        if (datos) {
            console.log(datos);
            selcategoria.innerHTML = '';
            const categorias = datos.categorias; 
           // if(categorias.length>0) {       
            const apt = crearElementoTexto("option", selcategoria, "Buscar categoría");
            apt.setAttribute("value", "");
            categorias.forEach( categoria => {                
                const nuevooption = crearElementoTexto("option", selcategoria, categoria.nombre)
                nuevooption.setAttribute("value", categoria.id);
            });
       /* }else{
            let fila=crearElementoTexto('tr',bodytabla);
                crearElementoTexto('td',fila, "No disponemos libros de esa categoria");
        }*/
        }
    }
}
function realizaPeticionTodas(){
    let peticion2=new XMLHttpRequest();
    url="/api/libro/todas";       
    peticion2.open('POST', url);     
    peticion2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");       
    peticion2.addEventListener('load',() => procesaTodasCategoriasRespuesta(peticion2));
    peticion2.send();
}
function procesaTodasCategoriasRespuesta(peti){    
    if(peti.status===200){
        const datos=JSON.parse(peti.responseText);
        if (datos) {
            console.log(datos);            
            selcategoria.innerHTML = '';
            const categorias = datos; 
            const apt = crearElementoTexto("option", selcategoria, " Buscar categoría");
            apt.setAttribute("value", "");
            for(let i=0;i<categorias.length; i++){
                const nuevooption = crearElementoTexto("option", selcategoria, categorias[i].nombre)
                nuevooption.setAttribute("value", categorias[i].id);
            }
        }
    }
}
function mostrarLibros(libros){    
    if(libros!=null){     
        bodytabla.innerHTML = "";        
        for(let i=0; i<libros.length; i++){            
            let fila=crearElementoTexto('tr',bodytabla);
            crearElementoTexto('td',fila, libros[i].id);
            crearElementoTexto('td',fila, libros[i].titulo);
            crearElementoTexto('td',fila, libros[i].autor.nombre + ' '+ libros[i].autor.apellido);
            crearElementoTexto('td',fila, libros[i].categoria.nombre);
            crearElementoTexto('td',fila, libros[i].isbn);
            crearElementoTexto('td',fila, libros[i].ubicacion);
            let td_detalles=crearElementoTexto('td',fila);            
            let detalles=crearElementoTexto('button',td_detalles,"detalles");
            detalles.value = libros[i].id;
            detalles.className='btnDetRes';
            detalles.name = 'detalles';
            detalles.addEventListener('click', mostrarDetalles);
            let td_reserva=crearElementoTexto('td',fila);            
            let reserva=crearElementoTexto('button',td_reserva,"Reserva");
            reserva.value = libros[i].id;
            reserva.className='btnDetRes';
            reserva.name = 'reserva';
            reserva.addEventListener('click', realizaReserva);
        }
    }/*else{
        let fila=crearElementoTexto('tr',bodytabla);
            crearElementoTexto('td',fila, "No disponemos libros de esa categoria");
       /* const mensaje1 = document.getElementById("mensajeerror");
    todapantallaerror.style.display="flex";
    mensaje1.style.display = "block";
    const textomensaje1 = document.getElementById("textomensajeerror");
    textomensaje1.textContent = "No disponemos libros de esa categoria";
    document.getElementById("titulomensajeerror").textContent = "ERROR";}
    }*/
    
}
function mostrarDetalles(e){
    e.preventDefault ();   
   
    const idLibro = e.target.value;
    fetch('/api/libros/' + idLibro,{
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "GET",        
    }).then( res => {
        res.json()
        .then( data => {            
            console.log(data)
            localStorage.setItem("detalle", JSON.stringify(data));            
            todapantallapopup.style.display="flex";
            popup.style.display="block";
            let valorId=document.getElementById("valorId");
            valorId.setAttribute("value",data.id) ;
            let valorTitulo=document.getElementById("valorTitulo");
            valorTitulo.setAttribute("value",data.titulo) ;
            let valorAutor=document.getElementById("valorAutor");
            valorAutor.setAttribute("value",data.autor.nombre + ' '+ data.autor.apellido) ;
            let valorIsbn=document.getElementById("valorIsbn");
            valorIsbn.setAttribute("value",data.isbn) ;
            let valorEditorial=document.getElementById("valorEditorial");
            valorEditorial.setAttribute("value",data.editorial) ;
            let valorGenero=document.getElementById("valorGenero");
            valorGenero.setAttribute("value",data.categoria.genero.nombre) ;
            let valorCategoria=document.getElementById("valorCategoria");
            valorCategoria.setAttribute("value",data.categoria.nombre) ;
            let valorUbicacion=document.getElementById("valorUbicacion");
            valorUbicacion.setAttribute("value",data.ubicacion) ;
            let valorSituacion=document.getElementById("valorSituacion");
            valorSituacion.setAttribute("value",data.libroSituacion) ;           
            
        })
        .catch( err => console.log(err))
        
    }).catch(err => {        
        if (err.status == 406) {
            console.log("Libro no encontrado")
        }
    })
    
}
window.addEventListener('load',function(){
    selgenero=document.getElementById("selgenero");
    selcategoria=document.getElementById("selcategoria");
    let preselautor=document.getElementById("selautor");
    let cerrarDetalles=document.getElementById("cerrarDetalles");
    todapantallapopup=document.getElementById("popupcontenedor");
    todapantallaerror=document.getElementById("errorcontenedor");
    let popup = document.getElementById("popup");
    bodytabla=document.getElementById('bodytabla');
    todapantallapopup2=document.getElementById("popupcontenedor2");    
    let popup2 = document.getElementById("popup2");

    const botonesDetalles = document.getElementsByName('detalles'); 
    for(let i = 0; i < botonesDetalles.length; i++) {
        botonesDetalles[i].addEventListener('click', mostrarDetalles);
    }  
    const botonesReserva = document.getElementsByName('reserva');     
    for(let i = 0; i < botonesReserva.length; i++) {
        botonesReserva[i].addEventListener('click', realizaReserva);
    }
    

    selgenero.addEventListener('change', function(){  
        if (selgenero.value == '') {
            realizaPeticionTodas();
        }
        else {
            realizaPeticionAjax('genero',selgenero.value);        
            realizaPeticionCategorias(selgenero.value);
        }
    })
    
    selcategoria.addEventListener('change', function(){        
        if (selcategoria.value != '') {
            realizaPeticionAjax('categoria',selcategoria.value);
        }        
    })
    preselautor.addEventListener('change', function(){
        const au = preselautor.value;
        selautor=document.querySelector("#autorList option[value='"+au+"']").dataset.value;
        realizaPeticionAjax('autor',selautor);
        //console.log(selautor);
    }) 
    todapantallapopup.addEventListener("click", function(){
        this.style.display="none";
    });

    cerrarDetalles.addEventListener("click", function(){
       todapantallapopup.style.display="none";
    });
    todapantallaerror.addEventListener("click", function(){
        this.style.display="none";
    });
    cerrarError.addEventListener("click", function(){
        todapantallaerror.style.display="none";
     });
    
    popup.addEventListener("click", function (e) {
        e.stopPropagation();
    })
    todapantallapopup2.addEventListener("click", function(){
        this.style.display="none";
    });
    popup2.addEventListener("click", function (e) {
        e.stopPropagation();
    })
    cerrarReserva.addEventListener("click", function(){
        todapantallapopup2.style.display="none";
     });
})

window.addEventListener('resize', function(){    
    todapantallapopup.style.display="none";
})