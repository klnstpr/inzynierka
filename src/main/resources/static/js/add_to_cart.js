$(document).ready.(function()){
 $("#buttonAdd2Cart").on("click", function(e)){
 addToCart();
 });
 });

 function addToCart(){
 //quantity= $("#quantity" + productId).val();
 alert("Hello! I am an alert box!");
 url=contextPath + "koszyk/add/" + productId + "/" + quantity;

 $.ajax({
 type: "POST",
 url: url;
 });

 }