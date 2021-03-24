
var months = new Array ("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre");

var WeekDays = new Array("Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado");


var f = new Date();  

var strDate=WeekDays[f.getDay()] + " - " + months[f.getMonth()] + " " + f.getDate() +  ", " +f.getFullYear();

$(function() {
 $("h3+h3").text(strDate);
 });