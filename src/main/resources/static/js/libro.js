window.addEventListener('load',function(){

const btnLibroReserva=document.getElementById("btnLibroReserva");

var pintarestrella=document.getElementById("pintarestrella");

pintarEstrella();
btnLibroReserva.addEventListener('click', realizaReserva);  

//calificar(item);




})

function pintarEstrella(){
    var numero=document.getElementById("totalVotacion").innerHTML;
    console.log(numero);
    for(let j=0; j<5-numero;j++){
        for(let i=0; i<numero;i++){ 
            let star=crearElementoTexto('span',pintarestrella);            
            iconostar=crearElementoTexto('i',star)
            iconostar.setAttribute("class","fas fa-star yellow"); 
        }
        let star=crearElementoTexto('span',pintarestrella);
            star.setAttribute("class","far fa-star");
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
