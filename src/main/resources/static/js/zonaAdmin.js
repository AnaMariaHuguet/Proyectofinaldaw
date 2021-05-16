
window.addEventListener('load',function(){
    var acc = document.getElementsByClassName("acordeon");
var i;

for (i = 0; i < acc.length; i++) {
  acc[i].addEventListener("click", function() {
    /* Alternar entre agregar y eliminar la clase "activa",
    para resaltar el botón que controla el panel */
    this.classList.toggle("active");

    /* Alternar entre ocultar y mostrar el panel activo */
    var panel = this.nextElementSibling;
    if (panel.style.display === "block") {
        panel.style.display = "none";
      } else {
        panel.style.display = "block";
      }
    if (panel.style.maxHeight) {
        panel.style.maxHeight = null;
      } else {
        panel.style.maxHeight = panel.scrollHeight + "px";
      }   
  });
}
})
