

SENAI.vendas = new Object();

$(document).ready(function(){

	SENAI.PATH = "/Senai/rest/"

	SENAI.vendas.data = function(){
		// Obtém a data/hora atual
		var data = new Date();

		// Guarda cada pedaço em uma variável
		var dia     = data.getDate();           // 1-31
		var dia_sem = data.getDay();            // 0-6 (zero=domingo)
		var mes     = data.getMonth();          // 0-11 (zero=janeiro)
		var ano2    = data.getYear();           // 2 dígitos
		var ano4    = data.getFullYear();       // 4 dígitos
		var hora    = data.getHours();          // 0-23
		var min     = data.getMinutes();        // 0-59
		var seg     = data.getSeconds();        // 0-59
		var mseg    = data.getMilliseconds();   // 0-999
		var tz      = data.getTimezoneOffset(); // em minutos

		// Formata a data e a hora (note o mês + 1)
		var str_hora = hora + ':' + min + ':' + seg;
		var str_data = ano4 + '-' + (mes+1) + '-' + dia + ' ' + str_hora;
		
		
		return(str_data);
	}
	
	SENAI.vendas.buscarIdFuncionario = function(){
		var email = sessionStorage.getItem('email');
		$.ajax({
			type: "GET",
			url: SENAI.PATH + "vendas/checkEmail",
			data: "email="+email,
			success: function(funcionario){
				
				var id = funcionario.id;
				
				sessionStorage.setItem('idFunc', id );
				
				

			},
			error: function(info){
				var a="Erro ao consultar os cadastros de usuário: "+info.status+" - "+info.statusText;
				var b = a.replace(/'/g, '');
				Swal.fire(b);
			}
		})
	}
	SENAI.vendas.buscarIdFuncionario();
	
	
	

	SENAI.vendas.iniciarVenda = function(){
		
		console.log(document.frmAddCadVendas.totalValor.value);
		if(document.frmAddCadVendas.totalValor.value == null){
			
		}else{
		
		var idFunc = sessionStorage.getItem('idFunc');
		var inicioVenda = new Object();

		inicioVenda.data = SENAI.vendas.data();
		inicioVenda.idFuncionario = idFunc;
		
		$.ajax({
				type: "POST",
				url: SENAI.PATH + "vendas/inserir",
				data:JSON.stringify(inicioVenda),
				success:function(msg){
					//Swal.fire(msg);
				
				},
				error:function(info){
					Swal.fire("Erro ao iniciar uma venda: "+ info.status + " - "+ info.statusText);	
				}
			});	
		}
		
	}
	
	SENAI.vendas.finalizarVenda = function(){
		
		var valorPago = document.frmFinalizaVendas.valorPagCliente.value;
		var valorTotal = document.frmAddCadVendas.totalValor.value;
		
		var modalFinalizaVendas = {
			
				title: "Finalizar Venda",
				height: 280,
				width: 400,
				modal: true,
				buttons:{
					"Concluir":function(){
					
					if(SENAI.vendas.validarFinal() == false){
						Swal.fire({
  							position: 'center',
  							icon: 'error',
  							title: 'Valor Pago menor que Valor Total!',
  							showConfirmButton: true
						})		
					
					}else{
						Swal.fire({
  							position: 'center',
  							icon: 'success',
  							title: 'Venda Concluída com Sucesso!',
  							showConfirmButton: false,
  							timer: 1500
						})		
						$("#modalFinalizaVendas").dialog("close");			
						SENAI.vendas.iniciarVenda();
						SENAI.carregaPagina("Content/outrosCadastros");
						
					}
		
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
		$("#modalFinalizaVendas").dialog(modalFinalizaVendas);
		
	}
	
	SENAI.vendas.validarFinal = function(){
		var valorPago = document.frmFinalizaVendas.valorPagCliente.value;
		var valorTotal = document.frmAddCadVendas.totalValor.value;
		
		var resultado = false;
		
		if(valorPago < valorTotal){
			resultado = false;
		}else{
			resultado = true;
		}
		
		return resultado;
		
	}
	
	SENAI.vendas.buscarIdVendaAtual = function(){
		var valorBusca = $("#campoBusca").val();
		$.ajax({
			type: "GET",
			url: SENAI.PATH + "vendas/buscar",
			data: "valorBusca="+valorBusca,
			success: function(vendas){

				vendas = JSON.parse(vendas);
				
				var idF = new Object();
				
				var idFunc = sessionStorage.getItem('idFunc');
				var data = SENAI.vendas.data();
				idF.idFuncionario = idFunc;
				idF.data = data;
				
				var idFuncionario = vendas[0].idFuncionario;
				var idVendas = vendas[0].idVendas;
				
				
					$.ajax({
						type:"PUT",
						url: SENAI.PATH + "vendas/alterarIdFunc",
						data: JSON.stringify(idF),
						success: function(msg){
							//Swal.fire(msg);
							
						
						
						},
						error: function(info){
							Swal.fire("Erro ao editar idFuncionario: "+ info.status+" - "+info.statusText);
						}
					});
				
				
				
				document.frmAddCadVendas.idVenda.value = idVendas;			
				document.getElementById("idVend").innerHTML = idVendas;	
			},
			error: function(info){
				Swal.fire("Erro ao buscar id da Venda: "+info.status+" - "+info.statusText);
			}
		});
		
	}
	SENAI.vendas.buscarIdVendaAtual();
	SENAI.vendas.inserirProdVendas = function(){

		nome = document.frmAddCadVendas.produto.value;
		quantidade = document.frmAddCadVendas.quantidade.value;
		precoU = document.frmAddCadVendas.vlrunitario.value;
		precoT = document.frmAddCadVendas.vlrtotal.value;
		quantMaxima = document.frmAddCadVendas.quantMaxima.value;

		if(precoT==""||precoU==""||nome==""||quantidade==""){
			Swal.fire({
				icon: 'error',
				title: 'Oops...',
				text: 'Preencha todos os campos.'
			})
		}else if(quantidade>quantMaxima){
			Swal.fire({
  				position: 'center',
  				icon: 'error',
  				title: 'Quantidade não pode ser maior que o valor do Estoque!',
  				showConfirmButton: true
			})		
		}else if(precoU<0 | precoT<0){
			Swal.fire({
  				position: 'center',
  				icon: 'error',
  				title: 'Preço não pode ser Negativo!',
  				showConfirmButton: true
			})		
		}else{


			var produtosVenda = new Object();
			produtosVenda.idVendas = document.frmAddCadVendas.idVenda.value;
			produtosVenda.idProdutos = document.frmAddCadVendas.idProdutos.value;
			produtosVenda.quantidadeVenda = document.frmAddCadVendas.quantidade.value;
			var vlrTotal = document.frmAddCadVendas.vlrtotal.value;
			var conv1 = vlrTotal.replace("R$","");
			var conv2 = conv1.replace(",",".");
			var valorTotal = parseFloat(conv2);
			produtosVenda.valorVenda = valorTotal;

			$.ajax({
				type: "POST",
				url: SENAI.PATH + "vendas/inserirProdVendas",
				data:JSON.stringify(produtosVenda),
				success:function(msg){
					$("#addCadVendas").trigger("reset");
					SENAI.vendas.buscarProdVendas();
					SENAI.produtos.buscar();
					//Swal.fire(msg);
					
				},
				error:function(info){
					Swal.fire("Erro ao cadastrar um novo produto na venda: "+ info.status + " - "+ info.statusText);	
				}
			});	
		}
	}
	
	
	SENAI.vendas.buscarProdVendas = function(){
		var valorBusca = $("#campoBusca").val();
		$.ajax({
			type: "GET",
			url: SENAI.PATH + "vendas/buscarProdVendas",
			data: "valorBusca="+valorBusca,
			success: function(dados){

				dados = JSON.parse(dados);

				$("#listaProdVendas").html(SENAI.vendas.exibir(dados));
				SENAI.vendas.buscarValorTotal();
				
				
				

			},
			error: function(info){
				Swal.fire("Erro ao consultar os cadastros de produto: "+info.status+" - "+info.statusText);
			}
		});
		SENAI.vendas.exibir = function(listaDeProdVenda, valorTotal){
			var tabela = 
				"<table>"+
				"<tr>"+	
				"<th> Id</th>"+
				"<th> Nome</th>"+
				"<th> Categoria</th>"+
				"<th> Quantidade</th>"+
				"<th> Preço Unitário</th>"+
				"<th> Preço Total</th>"+
				"<th class='acoes'>Excluir</th>"+
				"</tr>";

			if(listaDeProdVenda != undefined && listaDeProdVenda.length >0){


				for(var i=0; i<listaDeProdVenda.length; i++){

					tabela+="<tr>"+	
					"<td>"+listaDeProdVenda[i].idProdutos+"</td>"+
					"<td>"+listaDeProdVenda[i].nome+"</td>"+
					"<td>"+listaDeProdVenda[i].categoria+"</td>"+
					"<td>"+listaDeProdVenda[i].quantidadeVenda+"</td>"+
					"<td>"+listaDeProdVenda[i].precoUni.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })+"</td>"+
					"<td>"+listaDeProdVenda[i].valorVenda.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })+"</td>"+
					"<td>"+
					"<a onclick=\"SENAI.vendas.exibirEdicao('"+listaDeProdVenda[i].id+"')\"><img src='css/img/edit.png' alt='Editar'></a>" +
					"<a onclick=\"SENAI.vendas.excluir('"+listaDeProdVenda[i].id+"')\"><img src='css/img/delete.png' alt='Apagar'></a>" +
					"</td>"+
					"</tr>";
				}
				tabela+= "<tr>"+	
				
				"<th colspan=4>TOTAL </th>"+
				"<th colspan=3 id=\"valorTotal\">R$ </th>"+
				
				"</tr>";
				tabela+= "<tr>"+	
				"<th style=\"background-color:#f44336;cursor:pointer;\" colspan=4 onclick=\"SENAI.vendas.excluirVenda();\">Cancelar Venda</th>"+
				"<th style=\"background-color:#4CAF50;cursor:pointer;\" colspan=3 onclick=\"SENAI.vendas.finalizarVenda();\">Finalizar Venda</th>"+
				//"<th id='finVenda' colspan=3><button type=\"button\" onclick=\"SENAI.vendas.finalizarVenda()\" >Finalizar Venda</button></th>"+
				"</tr>";

			}else if (listaDeProdVenda == ""){
				tabela += "<tr><td colspan='6'>Nenhum Produto Adicionado na Venda</td></tr>";
			}
			tabela +="</table>";

			return tabela;

			$("#listaProdVendas").html(tabela);
		}
	}
	SENAI.vendas.buscarProdVendas();
	SENAI.vendas.buscarValorTotal = function(){
		var valorBusca = $("#campoBusca").val();
	
		$.ajax({
			type: "GET",
			url: SENAI.PATH + "vendas/buscarValorTotal",
			data: "valorBusca="+valorBusca,
			success: function(valorTotal){

				valorTotal = JSON.parse(valorTotal);
	
				document.getElementById("valorTotal").innerHTML = valorTotal[0].valorTotalVenda.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
				
				var troco = document.frmFinalizaVendas.valorPagCliente.value - valorTotal[0].valorTotalVenda;
				document.frmAddCadVendas.totalValor.value = valorTotal[0].valorTotalVenda;
				
				if(document.frmFinalizaVendas.valorPagCliente.value >= valorTotal[0].valorTotalVenda){
					document.getElementById("idTroco").innerHTML = "Troco: "+troco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
				}else{
					document.getElementById("idTroco").innerHTML = "";
				}
				
			},
			error: function(info){
				Swal.fire("Erro ao consultar o valor total da venda: "+info.status+" - "+info.statusText);
			}
		});
		
		
	}
	SENAI.vendas.buscarValorTotal();
	
	SENAI.vendas.exibirEdicao = function(id){
		$.ajax({
			type:"GET",
			url: SENAI.PATH +"vendas/checkIdProdVendas",
			data: "id="+id,
			success: function(vendas){
					document.frmEditaProdVendas.id.value= vendas.id;
					document.frmEditaProdVendas.idProdutos.value= vendas.idProdutos;			
					document.frmEditaProdVendas.quantidade.value = vendas.quantidadeVenda;
					document.frmEditaProdVendas.valorU.value = (vendas.valorVenda/vendas.quantidadeVenda);
					quantMaxima = document.frmAddCadVendas.quantMaxima.value;
					
					console.log(vendas);
					
					var modalEditaCadProdVendas = {
							title: "Alterar Quantidade",
							height: 185,
							width: 400,
							modal: true,
							buttons:{
								"Salvar":function(){
									
									if(document.frmEditaProdVendas.quantidade.value < 1){
										Swal.fire({
  											position: 'center',
  											icon: 'error',
  											title: 'Quantidade não pode ser menor que 1!',
  											showConfirmButton: true
										})		
									//}else if(document.frmEditaProdVendas.quantidade.value > quantMaxima){
									//	Swal.fire({
  									//		position: 'center',
  									//		icon: 'error',
  									//		title: 'Quantidade não pode ser maior que o estoque!',
  									//		showConfirmButton: true
									//	})				
									}else{
										SENAI.vendas.editar();	
									}		
											
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
					$("#modalEditaProdVendas").dialog(modalEditaCadProdVendas);
		
			},
			error: function(info){
				Swal.fire("Erro ao buscar cadastro para edição: "+info.status+" - "+info.statusText);
			}

		});
	}
	
	SENAI.vendas.editar = function(){
		 
		var prodVendas = new Object();
		prodVendas.id = document.frmEditaProdVendas.id.value;
		prodVendas.idProdutos = document.frmEditaProdVendas.idProdutos.value;
		prodVendas.quantidadeVenda = document.frmEditaProdVendas.quantidade.value;
		var valorU = document.frmEditaProdVendas.valorU.value;
		prodVendas.valorVenda = prodVendas.quantidadeVenda*valorU;
		
	
		$.ajax({
			type:"PUT",
			url: SENAI.PATH + "vendas/alterar",
			data:JSON.stringify(prodVendas),
			success: function(msg){
				//Swal.fire(msg);
				Swal.fire({
  					position: 'center',
  					icon: 'success',
  					title: 'Quantidade Alterada com Sucesso!',
  					showConfirmButton: false,
  					timer: 1500
				})	
				SENAI.vendas.buscarProdVendas();
				$("#modalEditaProdVendas").dialog("close");
			},
			error: function(info){
				Swal.fire("Erro ao editar produto: "+ info.status+" - "+info.statusText);
			}
		});
	};
	
	SENAI.vendas.excluir = function(id){
		$.ajax({
			type:"DELETE",
			url: SENAI.PATH +"vendas/excluir/"+id,
			success: function(msg){
				Swal.fire(msg);
				SENAI.vendas.buscarProdVendas();
			},
			error: function(info){
				Swal.fire("Erro ao excluir produto da Venda: " + info.status + " - " + info.statusText);
			}
		});
	};
	
	SENAI.vendas.excluirVenda = function(){
		
		
		Swal.fire({
  			title: 'Você tem Certeza?',
  			text: "Você não poderá reverter isso!",
  			icon: 'warning',
  			showCancelButton: true,
  			confirmButtonColor: '#4CAF50',//'#58AF9B'//'#3085d6',
  			cancelButtonColor: '#f44336',//'#d33',
  			confirmButtonText: 'Sim, Cancelar a Venda!',
			cancelButtonText: 'Voltar',
		}).then((result) => {
  		if (result.isConfirmed) {
    	
			$.ajax({
			type:"DELETE",
			url: SENAI.PATH +"vendas/excluirVenda",
			success: function(msg){
				//Swal.fire(msg);
				Swal.fire({
  					position: 'center',
  					icon: 'success',
  					title: 'Venda Cancelada com Sucesso!',
  					showConfirmButton: false,
  					timer: 1500
				})		
				document.frmAddCadVendas.totalValor.value = 3;
				var idFuncionario = document.frmAddCadVendas.idFuncionario.value;
				SENAI.vendas.buscarProdVendas();
				SENAI.vendas.iniciarVenda(idFuncionario);
				SENAI.carregaPagina("Content/outrosCadastros");
			},
			error: function(info){
				Swal.fire("Erro ao excluir produto da Venda: " + info.status + " - " + info.statusText);
			}
		})
    
  }
})
		
	};
	
	
	
		
	
	
})