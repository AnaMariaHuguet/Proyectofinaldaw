window.addEventListener('load',function(){
       
    pintarEstrella();  
    
})

function pintarEstrella(){
    const pintarestrella = document.getElementById("pintarestrella");
    const numero = document.getElementById("puntuacion").innerHTML;
    const iconoVotado = numero < 3 ? "fas fa-thumbs-down yellow" : "fas fa-thumbs-up yellow";
    const iconoVacio = "far fa-thumbs-up";
    
    for ( let i = 0 ; i < 5 ; i++) {
        let star=crearElementoTexto('span', pintarestrella);
        iconostar=crearElementoTexto('i', star);
        star.setAttribute("class", i < numero ? iconoVotado : iconoVacio);        
    } 
      
}

