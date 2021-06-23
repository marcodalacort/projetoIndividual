SENAI.funcionario = new Object();

$(document).ready (function(){

	SENAI.PATH = "/Senai/rest/";

	validaCampos = function(){

		var nome = document.frmAddFuncionario.nome.value;
		var expRegNome = new RegExp(/[A-zÀ-ü]{3,}([ ]{1}[A-zÀ-ü]{2,})|([A-zÀ-ü]{3,})+$/);
		if (!expRegNome.test(nome)){
			Swal.fire('Preencha o campo Nome corretamente.')
			document.frmAddFuncionario.nome.focus();
			return false;
		}
		
		var email = document.frmAddFuncionario.email.value;
		var expRegEmail = new RegExp(/^[a-z0-9.]+@[a-z0-9]+\.[a-z]+\.([a-z]+)?$/i);
		if (!expRegNome.test(email)){
			Swal.fire('Preencha o campo Email corretamente.')
			document.frmAddFuncionario.email.focus();
			return false;
		}
		
		var cargoId = document.frmAddFuncionario.cargoId.value;


		var senha = document.frmAddFuncionario.senhaFun.value;
		var senhaRep = document.frmAddFuncionario.senhaRepFun.value;
		var expRegSenha = new RegExp(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/);

		if(senhaRep==""||senha==""){
			Swal.fire({
				icon: 'error',
				title: 'Oops...',
				text: 'O campo da senha esta vazio.'
			})
		}else{
			if (!expRegSenha.test(senha)){
				Swal.fire('A senha deve ter letras e números!')
				document.frmAddFuncionario.senhaFun.focus();
			}else{
				if (senhaRep==senha&&!senhaRep==""){
					SENAI.funcionario.cadastrar();
					$("#addFuncionario").trigger("reset");
				}else{
					Swal.fire({
						icon: 'error',
						title: 'Oops...',
						text: 'A senha tem que ser igual.'
					})

					document.frmAddFuncionario.senhaRepFun.focus();
				}
			}

		}
	}


SENAI.funcionario.carregaCargo = function(id){

		if(id!=undefined){
			selectC = "#selCargoEditar";
		}else{
			selectC = "#selCargo";
		}

		$.ajax({
			type:"GET",
			url: SENAI.PATH + "funcionario/buscarSelC",
			success:function(cargos){

				if(cargos!=""){
					$(selectC).html("");
					var option = document.createElement("option");
					option.setAttribute ("value","");
					option.innerHTML = ("Escolha");
					$(selectC).append(option);

					for (var i=0;i<cargos.length;i++){
						var option = document.createElement("option");
						option.setAttribute ("value",cargos[i].id);

						if((id!=undefined)&&(id==cargos[i].id))
							option.setAttribute ("selected", "selected");

						option.innerHTML = (cargos[i].nome);
						$(selectC).append(option);

					}
				}else{

					$(selectC).html("");
					var option = document.createElement("option");
					option.setAttribute ("value","");
					$(selectC).append(option);
					$(selectC).addClass("aviso");

				}


			},
			error:function(info){
				SENAI.exibirAviso("Erro ao buscar os cargos: "+info.status+" - "+info.statusText);
				$(selectC).html(".");
				var option = document.createElement("option");
				option.setAttribute ("value","");
				option.innerHTML = ("Erro ao carregar cargos!");
				$(selectC).append(option);
				$(selectC).addClass("aviso");
			}
		});
	}
	SENAI.funcionario.carregaCargo();
	SENAI.funcionario.buscar = function(){
		var valorBusca = $("#campoBusca").val();
		$.ajax({
			type: "GET",
			url: SENAI.PATH + "funcionario/buscar",
			data: "valorBusca="+valorBusca,
			success: function(dados){

				dados = JSON.parse(dados);
				
				var cabecalho = 
				"<table>"+
				"<tr>"+	
				"<th class='nome'> Nome</th>"+
				"<th class='email'> Email</th>"+
				"<th class='cargo'> Cargo</th>"+
				"<th class='acoes'>Editar</th>"+
				"<th class='senha'>Senha</th>"+
				"</tr>"+
				"</table>";
				
				$("#listaCab").html(cabecalho);
				$("#listaFuncionarios").html(SENAI.funcionario.exibirFun(dados));
				$(document).ajaxComplete(function () {
        			paginate('#listaFuncionarios',2);
        			function paginate(tableName,RecordsPerPage) {
            			$('#nav').remove();
            			$(tableName).after('<div id="nav"></div>');
            			var rowsShown = RecordsPerPage;
            			var rowsTotal = $(tableName + ' tbody tr').length;
            			var numPages = rowsTotal / rowsShown;
            			for (i = 0; i < numPages; i++) {
                			var pageNum = i + 1;
                			$('#nav').append('<a href="#" rel="' + i + '">' + pageNum + '</a> ');
           				}
       	     			$(tableName + ' tbody tr').hide();
            			$(tableName + ' tbody tr').slice(0, rowsShown).show();
            			$('#nav a:first').addClass('active');
            			$('#nav a').bind('click', function () {
                			$('#nav a').removeClass('active');
                			$(this).addClass('active');
                			var currPage = $(this).attr('rel');
                			var startItem = currPage * rowsShown;
                			var endItem = startItem + rowsShown;
                			$(tableName + ' tbody tr').css('opacity', '5.0').hide().slice(startItem, endItem).
                    		css('display', 'table-row').animate({ opacity: 1 }, 300);
            			});
        			}
   				});

			},
			error: function(info){
				SENAI.exibirAviso("Erro ao consultar os funcionarios: "+info.status+" - "+info.statusText);
			}
		});

		SENAI.funcionario.exibirFun = function(listaDeFuncionarios){
			var tabela = 
				"<table>";

			if(listaDeFuncionarios != undefined && listaDeFuncionarios.length >0){


				for(var i=0; i<listaDeFuncionarios.length; i++){

					tabela+="<tr>"+	
					"<td class='nome'>"+listaDeFuncionarios[i].nome+"</td>"+
					"<td class='email'>"+listaDeFuncionarios[i].email+"</td>"+
					"<td class='cargo'>"+listaDeFuncionarios[i].cargo+"</td>"+
					"<td class='acoes'>"+
					"<a onclick=\"SENAI.funcionario.exibirEdicao('"+listaDeFuncionarios[i].id+"')\"><img src='css/img/edit.png' alt='Editar'></a>" +
					"<a onclick=\"SENAI.funcionario.excluir('"+listaDeFuncionarios[i].id+"')\"><img src='css/img/delete.png' alt='Apagar'></a>" +
					"</td>"+
					"<td class='senha'><a onclick=\"SENAI.funcionario.exibirEdicaoSenha('"+listaDeFuncionarios[i].id+"')\"><img src='css/img/key.png' alt='Editar'></a></td>"+
					"</tr>";
				}

			}else if (listaDeFuncionarios == ""){
				tabela += "<tr><td colspan='6'>Nenhum registro encontrado</td></tr>";
			}
			tabela +="</table>";

			return tabela;
			"</table>";

			$("#listaFuncionarios").html(tabela);
		}
	}
	SENAI.funcionario.buscar();

	SENAI.funcionario.cadastrar = function(){

		var passCad = document.frmAddFuncionario.senhaFun.value;
		var emBase64Cad = btoa(passCad);
		var funcionario = new Object();
		funcionario.nome = document.frmAddFuncionario.nome.value;
		funcionario.email = document.frmAddFuncionario.email.value;
		funcionario.senha = emBase64Cad;
		funcionario.cargo = document.frmAddFuncionario.cargoId.value;


		$.ajax({
			type: "POST",
			url: SENAI.PATH + "funcionario/inserir",
			data:JSON.stringify(funcionario),
			success:function(msg){
				Swal.fire(msg);
				SENAI.funcionario.buscar();
			},
			error:function(info){
				Swal.fire({
					icon: 'error',
					title: 'Oops...',
					text: 'Nao foi cadastrado.'
				})	
			}
		});	
	}

	SENAI.funcionario.exibirEdicao = function(id){
		$.ajax({
			type:"GET",
			url: SENAI.PATH +"funcionario/checkId",
			data: "id="+id,
			success: function(funcionario){
			
				document.frmEditaFuncionario.idFuncionario.value=funcionario.id;			
				document.frmEditaFuncionario.nome.value = funcionario.nome;
				document.frmEditaFuncionario.email.value = funcionario.email;
				SENAI.funcionario.carregaCargo(funcionario.cargo);


				var modalEditaFuncionario = {
						title: "Editar Dados",
						height: 300,
						width: 600,
						modal: true,
						buttons:{
							"Salvar":function(){
								var nome = document.frmEditaFuncionario.nome.value;
								var expRegNome = new RegExp(/[A-zÀ-ü]{3,}([ ]{1}[A-zÀ-ü]{2,})|([A-zÀ-ü]{3,})+$/);
								if (!expRegNome.test(nome)){
									Swal.fire('Preencha o campo Nome com letras maiusculas e/ou minusculas.')
									document.frmEditaFuncionario.nome.focus();
									return false;
								}

								

								SENAI.funcionario.editar();							
							},
							"Cancelar": function(){
								$(this).dialog("close");
							}
						},
						close:function(){
							//caso o usuário simplesmente feche a caixa de edição
							//não deve acontecer nada
						}
				};
				$("#modalEditaFuncionario").dialog(modalEditaFuncionario);

			},
			error: function(info){
				SENAI.exibirAviso("Erro ao buscar cadastro para edição: "+info.status+" - "+info.statusText);
			}

		});
	}

	SENAI.funcionario.editar = function(){


		var funcionario = new Object();
		funcionario.id = document.frmEditaFuncionario.idFuncionario.value;
		funcionario.nome=document.frmEditaFuncionario.nome.value;
		funcionario.email=document.frmEditaFuncionario.email.value;
		funcionario.cargo=document.frmEditaFuncionario.cargoId.value;

		$.ajax({
			type:"PUT",
			url: SENAI.PATH + "funcionario/alterar",
			data:JSON.stringify(funcionario),
			success: function(msg){
				Swal.fire(msg);
				SENAI.funcionario.buscar();
				$("#modalEditaFuncionario").dialog("close");
			},
			error: function(info){
				Swal.fire("Erro ao editar cadastro: "+ info.status+" - "+info.statusText);
			}
		});
	};

	SENAI.funcionario.excluir = function(id){
		$.ajax({
			type:"DELETE",
			url: SENAI.PATH +"funcionario/excluir/"+id,
			success: function(msg){
				Swal.fire(msg);
				SENAI.funcionario.buscar();
			},
			error: function(info){
				Swal.fire("Erro ao excluir funcionario: " + info.status + " - " + info.statusText);
			}
		});
	};
	SENAI.funcionario.exibirEdicaoSenha = function(id){	
		$.ajax({
			type:"GET",
			url: SENAI.PATH +"funcionario/checkIdSenha",
			data: "id="+id,
			success: function(funcionario){
				document.frmEditaFuncionarioSenha.idFuncionarioSenha.value=funcionario.id;
				document.frmEditaFuncionarioSenha.senhaNova.value="";
				document.frmEditaFuncionarioSenha.senhaRep.value="";

				var modalEditaFuncionarioSenha = {
						title: "Trocar Senha",
						height: 300,
						width: 600,
						modal: true,
						buttons:{
							"Salvar":function(){
								var Senha = document.frmEditaFuncionarioSenha.senhaNova.value;
								var expRegSenha = new RegExp(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/);	

								if (!expRegSenha.test(Senha)){
									Swal.fire("Preencha o campo Senha com letras e numeros e minimo 6 caracteres.");
									document.frmEditaFuncionarioSenha.senhaNova.focus();
									return false;
								}	

								var SenhaRep = document.frmEditaFuncionarioSenha.senhaRep.value;

								if (Senha!=SenhaRep){
									Swal.fire("A Senha tem que ser igual.");
									document.frmEditaFuncionarioSenha.senhaRep.focus();
									return false;
								}	


								SENAI.funcionario.editarSenha();							
							},
							"Cancelar": function(){
								$(this).dialog("close");
							}
						},
						close:function(){
							//caso o usuário simplesmente feche a caixa de edição
							//não deve acontecer nada
						}
				};
				$("#modalEditaFuncionarioSenha").dialog(modalEditaFuncionarioSenha);

			},
			error: function(info){
				SENAI.exibirAviso("Erro ao buscar cadastro para edição: "+info.status+" - "+info.statusText);
			}

		});
	}
	SENAI.funcionario.editarSenha = function(){

		var passCad = document.frmEditaFuncionarioSenha.senhaNova.value;
		var emBase64Cad = btoa(passCad);

		var funcionario = new Object();
		funcionario.id = document.frmEditaFuncionarioSenha.idFuncionarioSenha.value;
		funcionario.senha=emBase64Cad;


		$.ajax({
			type:"PUT",
			url: SENAI.PATH + "funcionario/alterarSenha",
			data:JSON.stringify(funcionario),
			success: function(msg){
				Swal.fire(msg);
				SENAI.funcionario.buscar();
				$("#modalEditaFuncionarioSenha").dialog("close");
			},
			error: function(info){
				Swal.fire("Erro ao trocar senha: "+ info.status+" - "+info.statusText);
			}
		});
	};
});