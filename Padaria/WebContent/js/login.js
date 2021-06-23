SENAI = new Object();
SENAI.funcionario = new Object();

$(document).ready(function(){

	SENAI.PATH = "/Senai/rest/"


validaCamposLogin = function(){
		
		var nome = document.frmGerente.nome.value;
		var expRegNome = new RegExp(/[A-zÀ-ü]{3,}([ ]{1}[A-zÀ-ü]{2,})|([A-zÀ-ü]{3,})+$/);
			if (!expRegNome.test(nome)){
				Swal.fire('Preencha o campo Nome corretamente.')
				document.frmGerente.nome.focus();
				return false;
			}
			
	
//		var sen = document.frmGerente.senha.value;
//		var expRegMat = new RegExp("^[0-9]{1}$");
//		
//		if (!expRegMat.test(sen)){
//			Swal.fire("Preencha o campo Senha corretamente.");
//			document.frmGerente.senha.focus();
//			return false;
//		}	
//		
//		var email = document.frmGerente.email.value;
//		var expRegFone = new RegExp(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/);
//		
//		if (!expRegFone.test(email)){
//			Swal.fire("Preencha o campo Email corretamente.");
//			document.frmGerente.email.focus();
//			return false;
//		}	
		
}
SENAI.funcionario.cadastrar = function(){

		var passCad = document.frmGerente.senha.value;
		var emBase64Cad = btoa(passCad);
		var funcionario = new Object();
		funcionario.nome = document.frmGerente.nome.value;
		funcionario.senha = emBase64Cad;
		funcionario.email = document.frmGerente.email.value;
		funcionario.cargo = document.frmGerente.cargo.value;
				
				$.ajax({
					type: "POST",
					url: SENAI.PATH + "funcionario/inserir",
					data:JSON.stringify(funcionario),
					success:function(msg){
						alert(msg);
					},
					error:function(info){
						alert("Deu cero nao");	
						}
				});	
	};
	

})