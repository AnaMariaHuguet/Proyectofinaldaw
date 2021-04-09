
function mostrarCarrito(){
    console.log("carritto")
    
}

function realizaReserva( e ){
    e.preventDefault ();   
   
    const idLibro = e.target.value;
   
    fetch('/api/reserva',{
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify({id : idLibro}) 
    }).then( res => {
        res.json()
        .then( data => {            
            console.log(data)
            localStorage.setItem("reserva", JSON.stringify(data));
            mostrarReservas();
        })
        .catch( err => console.log(err))
        
    }).catch(err => {
        if (err.status == 410) {
            console.log("Ya se encuentra reservado")
        }
        if (err.status == 406) {
            console.log("Libro o usuario no encontradoss")
        }
    })
    //mostrarCarrito();
}

function mostrarReservas () {
    const carrito = JSON.parse(localStorage.getItem("reserva"));
    const bodytablaReserva = document.getElementById("tablareserva");
    bodytablaReserva.innerHTML="";

    if (carrito) {

        for(let i=0; i<carrito.length; i++){
            let fila=crearElementoTexto('tr',bodytablaReserva); 
            
            if(carrito[i].libro) {
                crearElementoTexto('td',fila,carrito[i].libro.id);
                crearElementoTexto('td',fila,carrito[i].libro.titulo); 
                let celdaBorrar=crearElementoTexto('td',fila, "Borrar");
                celdaBorrar.addEventListener('click',function(){
                
                    // Borrar de la base de datos y actualizar el localstorage
                    //mostrarReservas();
                
                })
            } 
        }
    }
} 
