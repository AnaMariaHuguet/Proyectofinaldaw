
var peticion, datosjson, opcion, url, selgenero, selcategoria, selautor, contenido,bodytabla;


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
        mostrarLibros(datosjson);           
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
            const apt = crearElementoTexto("option", selcategoria, "-categoria-");
            apt.setAttribute("value", "");
            categorias.forEach( categoria => {                
                const nuevooption = crearElementoTexto("option", selcategoria, categoria.nombre)
                nuevooption.setAttribute("value", categoria.id);
            });
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
            const apt = crearElementoTexto("option", selcategoria, "-categoria-");
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
            let td_button=crearElementoTexto('td',fila);            
            let reserva=crearElementoTexto('button',td_button,"reserva");
            reserva.value = libros[i].id;
            reserva.name = 'reserva';
            reserva.addEventListener('click', realizaReserva);
        }
    }else{
        mensajeError.innerHTML="Fallo al mostrar datos";
    }
}

window.addEventListener('load',function(){
    selgenero=document.getElementById("selgenero");
    selcategoria=document.getElementById("selcategoria");
    selautor=document.getElementById("selautor");
    bodytabla=document.getElementById('bodytabla');
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
    selautor.addEventListener('change', function(){
        realizaPeticionAjax('autor',selautor.value);
    }) 
})