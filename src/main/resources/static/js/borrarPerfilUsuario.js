window.addEventListener('load',function(){
    let borrar=document.getElementById("btnBorrarPerfil");
   var valorborrar=document.getElementById("btnBorrarPerfil").value;
    var todapantallaerror=document.getElementById("errorcontenedor"); 
    
    borrar.addEventListener("click", function(e){
        e.preventDefault();
        const mensaje = document.getElementById("mensajeerror");
        const textomensaje = document.getElementById("textomensajeerror");
        todapantallaerror.style.display="flex";
        mensaje.style.display = "block";
        const message = "¿Estás seguro de querer borrar este usuario?";
        textomensaje.textContent = message;
        document.getElementById("titulomensajeerror").textContent = "PELIGRO";
        const contenedorButton = crearElementoTexto("div",textomensaje);
        contenedorButton.className="contenedorButton";
        let buttonSi = crearElementoTexto("button",contenedorButton,"SI");
        let buttonNo = crearElementoTexto("button",contenedorButton,"NO");
        
        buttonSi.addEventListener("click", function(e){
            e.preventDefault();
            fetch('/api/usuarios/'+valorborrar,{
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "DELETE",                        
            })
           .then( res => {
                if (res.status == 200)
                    window.location.href = "/admin/usuario"; 
                else if (res.status == 409 ) {
                    salidaMensajeError("El usuario tiene préstamos en activo", "INFO" );
                }         
                else {                    
                    salidaMensajeError("Fallo algo. No se puede borrar");
                }           
            })
            .catch( err => {
                    console.log(err.response);
                    let mensaje = "Fallo algo. No se puede borrar";
                    salidaMensajeError(mensaje);
            })
       
        
        });
        buttonNo.addEventListener("click", function(e){
            e.preventDefault();            
            todapantallaerror.style.display="none";        
        });         
    });
});