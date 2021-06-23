
function validaCampos(){
	var senha = document.frmAddColaborador.senhaCol.value;
	var senhaRep = document.frmAddColaborador.senhaRepCol.value;
	
	if(senhaRep==""||senha==""){
		Swal.fire({
			icon: 'error',
			title: 'Oops...',
			text: 'O campo da senha esta vazio.'
		})
	}else{
		if (senhaRep==senha&&!senhaRep==""){
			alert("Esta certo")
			SENAI.funcionario.cadastrar();
		}else{
			Swal.fire({
				icon: 'error',
				title: 'Oops...',
				text: 'A senha tem que ser igual.'
			})

			document.frmAddColaborador.senhaColRep.focus();
		}
	}
}