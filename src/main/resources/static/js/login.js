
window.addEventListener('load',function(){
let botonOjo=document.getElementById("btnOjo");

          
botonOjo.addEventListener("click", function(e){  
    e.preventDefault(); 
    var tipo = document.getElementById("password");
    const icon = botonOjo.childNodes[1];
    if(tipo.type == "password"){
        tipo.type = "text";        
        icon.setAttribute("class", "fa fa-eye");
    }else{
        tipo.type = "password";
        icon.setAttribute("class", "fa fa-eye-slash");
    }   
});
})