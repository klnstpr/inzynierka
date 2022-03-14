$(document).ready.(function()){
 $("#buttonZaplac").on("click", function(e)){
 zaplac();
 });
 });

 function zaplac(){
 uwagi = $("#quantity" + productId).val();
 alert("Hello! I am an alert box!");
 url=contextPath + "zaakceptowana" + uwagi;

 $.ajax({
 type: "POST",
 url: url;
 });

 }