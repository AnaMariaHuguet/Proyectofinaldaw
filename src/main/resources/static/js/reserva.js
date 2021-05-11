window.addEventListener('load',function(){
    let btnAnularReserva=document.getElementById("anularreserva");
    let bodytablaReserva=document.getElementById("bodytablaReserva");
    


    btnAnularReserva.addEventListener("click", function(){       
        fetch('/api/reserva/'+this.value,{
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "DELETE",                        
        })
        .then( res => {
            res.json()
            .then( data => { 
                //borrar el padre tr del padre td  del button 
                let row=this.parentNode.parentNode;
                bodytablaReserva.deleteRow (row) ;
            })
        })
        .catch( err => {
                console.log(err.response);
               let mensaje = "Fallo algo. No se puede borrar";
               salidaMensajeError(mensaje);
        })
    } );

})