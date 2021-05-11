window.addEventListener('load',function(){


pintarEstrella();
calificar(item);


})

function pintarEstrella(){
    var pintarestrella=document.getElementById("pintarestrella");
    var numero=document.getElementById("puntuacion").innerHTML;  
    for(let j=0; j<5-numero;j++){
        for(let i=0; i<numero;i++){ 
            let star=crearElementoTexto('span',pintarestrella);            
            iconostar=crearElementoTexto('i',star)
            //iconostar.setAttribute("class","fas fa-book yellow"); 
            iconostar.setAttribute("class","fas fa-thumbs-up yellow");
            //iconostar.setAttribute("class","fas fa-star yellow");
                        
        }
        let star=crearElementoTexto('span',pintarestrella);            
            //star.setAttribute("class","fas fa-book black");
            star.setAttribute("class","far fa-thumbs-up");
            //star.setAttribute("class","far fa-star");
            
            star.style.color="black";
    }

}
function calificar(item){
    var contador;
    //capturamos el primer caracter del id '?'estrella
    contador=item.id[0];
    //capturamos el resto del id después del primer carácter
    let nombre=item.id.substring(1);
    for(let i=0; i<contador;i++){
        if(i<item){
            //añadimos uno al indice del bucle
            document.getElementById((i+1)+nombre).style.color="orange";
        }else{
            document.getElementById((i+1)+nombre).style.color="inherit";
        }
    }
}
