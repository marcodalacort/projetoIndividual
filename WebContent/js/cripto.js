function validaCampos(){

var pass = document.formul.pass.value;

var dados = document.getElementById("exampleInputEmail").value;
sessionStorage.setItem('email', dados );
// Convertendo para Base64
	
var emBase64 = btoa(pass);
document.formul.password.value=emBase64


}
