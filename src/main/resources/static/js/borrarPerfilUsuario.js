window.addEventListener('load',function(){
    let borrar=document.getElementById("btnBorrarPerfil");
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
console.log("ha dicho que sisisississiis")
        
        });
        buttonNo.addEventListener("click", function(e){
            e.preventDefault();
            console.log("ha dicho que nooooooooooooooooooo")
            todapantallaerror.style.display="none";        
        });         
    });
});