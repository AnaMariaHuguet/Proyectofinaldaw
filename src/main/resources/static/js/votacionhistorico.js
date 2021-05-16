function cambiaPuntuacion ( puntuacion, idHistorico ) {
    fetch('/api/votahistorico',{ 
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST", 
        body:  JSON.stringify({
            puntuacion: puntuacion,
            id: idHistorico
        })                      
    })  

    pintaEstrellas(puntuacion, idHistorico);
    
}

function pintaEstrellas ( numero, idHistorico ) {
    const pintarestrella = document.getElementById("pintarestrella" + idHistorico);
    pintarestrella.innerHTML = "";
    const iconoVotado = numero < 3 ? "fas fa-thumbs-down yellow" : "fas fa-thumbs-up yellow";
    const iconoVacio = "far fa-thumbs-up";
    
    for ( let i = 0 ; i < 5 ; i++) {
        let star=crearElementoTexto('span', pintarestrella);
        iconostar=crearElementoTexto('i', star);
        star.setAttribute("class",  "pointer hovyell " + (i < numero ? iconoVotado : iconoVacio));        
        star.addEventListener("click", function () {
            cambiaPuntuacion(i + 1, idHistorico);
        });
    }
}