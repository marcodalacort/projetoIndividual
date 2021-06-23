

SENAI.produtos = new Object();

$(document).ready(function(){

	SENAI.PATH = "/Senai/rest/"
	
	
	

		SENAI.produtos.carregaCategoria = function(id){

		if(id!=undefined){
			selectCat = "#categoriaEdicao";
		}else{
			selectCat = "#selCategoria";
		}

		$.ajax({
			type:"GET",
			url: SENAI.PATH + "cadCategoria/buscarSelCat",
			success:function(categoria){

				if(categoria!=""){
					$(selectCat).html("");
					var option = document.createElement("option");
					option.setAttribute ("value","");
					option.innerHTML = ("Escolha");
					$(selectCat).append(option);

					for (var i=0;i<categoria.length;i++){
						var option = document.createElement("option");
						option.setAttribute ("value",categoria[i].id);

						if((id!=undefined)&&(id==categoria[i].id))
							option.setAttribute ("selected", "selected");

						option.innerHTML = (categoria[i].nome);
						$(selectCat).append(option);

					}
				}else{

					$(selectCat).html("");
					var option = document.createElement("option");
					option.setAttribute ("value","");
					$(selectCat).append(option);
					$(selectCat).addClass("aviso");

				}


			},
			error:function(info){
				SENAI.exibirAviso("Erro ao buscar as categorias: "+info.status+" - "+info.statusText);
				$(selectCat).html(".");
				var option = document.createElement("option");
				option.setAttribute ("value","");
				option.innerHTML = ("Erro ao carregar categorias!");
				$(selectCat).append(option);
				$(selectCat).addClass("aviso");
			}
		});
	}
	SENAI.produtos.carregaCategoria();

	SENAI.produtos.cadastrar = function(){

		nome = document.frmCadProdutos.nome.value;
		preco = document.frmCadProdutos.preco.value;
		quantidade = document.frmCadProdutos.quantidade.value;
		categoriaId = document.frmCadProdutos.categoriaId.value;

		if(categoriaId==""||preco==""||nome==""||quantidade==""){
			Swal.fire({
				icon: 'error',
				title: 'Oops...',
				text: 'Preencha todos os campos.'
			})
		}else{


			var produtos = new Object();
			produtos.nome = document.frmCadProdutos.nome.value;
			produtos.preco = document.frmCadProdutos.preco.value;
			produtos.quantidade = document.frmCadProdutos.quantidade.value;
			produtos.categoriaId = document.frmCadProdutos.categoriaId.value;

			$.ajax({
				type: "POST",
				url: SENAI.PATH + "produtos/inserir",
				data:JSON.stringify(produtos),
				success:function(msg){
					$("#addCadProdutos").trigger("reset");
					Swal.fire(msg);
					SENAI.produtos.buscar();
				},
				error:function(info){
					Swal.fire("Erro ao cadastrar um novo produto: "+ info.status + " - "+ info.statusText);	
				}
			});	
		}
	}
	
	
	SENAI.produtos.buscar = function(){
		var valorBusca = $("#campoBusca").val();
		$.ajax({
			type: "GET",
			url: SENAI.PATH + "produtos/buscar",
			data: "valorBusca="+valorBusca,
			success: function(dados){

				dados = JSON.parse(dados);
				
				var cabecalho =
				"<table>"+
				"<tr>"+	
				"<th class='id'> Id</th>"+
				"<th class='nome'> Nome</th>"+
				"<th class='preco'> Preço</th>"+
				"<th class='categ'> Categoria</th>"+
				"<th class='quant'> Quantidade</th>"+
				"<th class='acoes'>Editar</th>"+
				"</tr>"
				"</table>";
				$("#listaCab").html(cabecalho);
				$("#listaProdutos").html(SENAI.produtos.exibir(dados));
				$(document).ajaxComplete(function () {
        			paginate('#listaProdutos',4);
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
				
				
				if(valorBusca == ""){
					$("#listaProdutosPDV").html("<center>Nenhum Produto Pesquisado</center>");
				}else{
					$("#listaProdutosPDV").html(SENAI.produtos.exibirPDV(dados));
				}
				

			},
			error: function(info){
				Swal.fire("Erro ao consultar os cadastros de produto: "+info.status+" - "+info.statusText);
			}
		});
		
		SENAI.produtos.exibir = function(listaDeProdutos){
			var tabela =
				"<table id=\"mytable\">";

			if(listaDeProdutos != undefined && listaDeProdutos.length >0){


				for(var i=0; i<listaDeProdutos.length; i++){

					tabela+="<tr>"+	
					"<td class='id'>"+listaDeProdutos[i].id+"</td>"+
					"<td class='nome'>"+listaDeProdutos[i].nome+"</td>"+
					"<td class='preco'>"+listaDeProdutos[i].preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })+"</td>"+
					"<td class='categ'>"+listaDeProdutos[i].categoria+"</td>"+
					"<td class='quant'>"+listaDeProdutos[i].quantidade+"</td>"+
					"<td class='acoes'>"+
					"<a onclick=\"SENAI.produtos.exibirEdicao('"+listaDeProdutos[i].id+"')\"><img src='css/img/edit.png' alt='Editar'></a>" +
					"<a onclick=\"SENAI.produtos.excluir('"+listaDeProdutos[i].id+"')\"><img src='css/img/delete.png' alt='Apagar'></a>" +
					"</td>"+
					"</tr>";
				}

			}else if (listaDeProdutos == ""){
				tabela += "<tr><td colspan='6'>Nenhum registro encontrado</td></tr>";
			}

			return tabela;

			$("#listaProdutos").html(tabela);
			
			
		}
		SENAI.produtos.exibirPDV = function(listaDeProdutos){
			
			var tabela = 
				"<table>"+
				"<tr>"+	
				"<th> Código</th>"+
				"<th> Nome</th>"+
				"<th> Preço</th>"+
				"<th> Categoria</th>"+
				"<th> Quantidade</th>"+
				"<th class='acoes'>Selecionar</th>"+
				"</tr>";
			

			if(listaDeProdutos != undefined && listaDeProdutos.length >0){


				for(var i=0; i<listaDeProdutos.length; i++){
					if(listaDeProdutos[i].quantidade<=0){
						tabela += "<tr><td colspan='6'>Produto "+listaDeProdutos[i].nome+" com código "+listaDeProdutos[i].id+" está Sem Estoque!</td></tr>";
					}else{
					tabela+="<tr>"+	
					"<td>"+listaDeProdutos[i].id+"</td>"+
					"<td>"+listaDeProdutos[i].nome+"</td>"+
					"<td>"+listaDeProdutos[i].preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })+"</td>"+
					"<td>"+listaDeProdutos[i].categoria+"</td>"+
					"<td>"+listaDeProdutos[i].quantidade+"</td>"+
					"<td>"+
					"<a onclick=\"SENAI.produtos.exibirEdicaoPDV('"+listaDeProdutos[i].id+"')\"><img src='css/img/edit.png' alt='Editar'></a>" +
					"</td>"+
					"</tr>";
				}
				}

			}else if (listaDeProdutos == ""){
				tabela += "<tr><td colspan='6'>Nenhum registro encontrado</td></tr>";
			}
			tabela +="</table>";

			return tabela;

			$("#listaProdutos").html(tabela);
		}
	}

	SENAI.produtos.buscar();
	SENAI.produtos.exibirEdicao = function(id){
		$.ajax({
			type:"GET",
			url: SENAI.PATH +"produtos/checkId",
			data: "id="+id,
			success: function(produtos){
					document.frmEditaCadProdutos.idProdutos.value=produtos.id;			
					document.frmEditaCadProdutos.nome.value = produtos.nome;
					document.frmEditaCadProdutos.preco.value = produtos.preco;
					document.frmEditaCadProdutos.quantidade.value = produtos.quantidade;
					SENAI.produtos.carregaCategoria(produtos.categoriaId);
					
					
					
					var modalEditaCadProdutos = {
							title: "Editar Produtos",
							height: 300,
							width: 600,
							modal: true,
							buttons:{
								"Salvar":function(){
									if(document.frmEditaCadProdutos.quantidade.value < 0){
										Swal.fire({
  											position: 'center',
  											icon: 'error',
  											title: 'Impossível adicionar valor negativo no Estoque!',
  											showConfirmButton: true
										})
									}else if(document.frmEditaCadProdutos.quantidade.value < produtos.quantidade){
										Swal.fire({
  											position: 'center',
  											icon: 'error',
  											title: 'Impossível adicionar valor menor que o atual no Estoque!',
  											showConfirmButton: true
										})
									}else if(document.frmEditaCadProdutos.preco.value < 0){
										Swal.fire({
  											position: 'center',
  											icon: 'error',
  											title: 'Impossível adicionar preço negativo no Produto!',
  											showConfirmButton: true
										})
									}else{
										SENAI.produtos.editar();		
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
					$("#modalEditaProdutos").dialog(modalEditaCadProdutos);
		
			},
			error: function(info){
				Swal.fire("Erro ao buscar cadastro para edição: "+info.status+" - "+info.statusText);
			}

		});
	}
	
	
	SENAI.produtos.calculo = function(){
		var valorQuant = $("#quantidade").val();
		var valorUnitario = $("#vlrunitario").val();
		
		var uni1 = valorUnitario.replace("R$","");
		var uni2 = uni1.replace(",",".");
		
		var vlrUnitario = parseFloat(uni2);
			
		var valorTotal = valorQuant*vlrUnitario;
	
		document.frmAddCadVendas.vlrtotal.value = valorTotal.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
	}
	
	SENAI.produtos.exibirEdicaoPDV = function(id){
		var valorQuant = $("#quantidade").val();
		$.ajax({
			type:"GET",
			url: SENAI.PATH +"produtos/checkId",
			data: "id="+id,
			success: function(produtos){
					document.frmAddCadVendas.idProdutos.value=produtos.id;			
					document.frmAddCadVendas.produto.value = produtos.nome;
					document.frmAddCadVendas.quantidade.value = 1;
					document.frmAddCadVendas.vlrunitario.value = produtos.preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
					document.frmAddCadVendas.vlrtotal.value = (produtos.preco * document.frmAddCadVendas.quantidade.value).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
					document.frmAddCadVendas.quantMaxima.value = produtos.quantidade;
			
		
			},
			error: function(info){
				Swal.fire("Erro ao buscar cadastro para edição: "+info.status+" - "+info.statusText);
			}

		});
	}
	
	
	
	SENAI.produtos.editar = function(){
		 
		
		var cadProdutos = new Object();
		cadProdutos.id = document.frmEditaCadProdutos.idProdutos.value;
		cadProdutos.nome = document.frmEditaCadProdutos.nome.value;
		cadProdutos.preco = document.frmEditaCadProdutos.preco.value;
		cadProdutos.categoriaId = document.frmEditaCadProdutos.categoriaId.value;
		cadProdutos.quantidade = document.frmEditaCadProdutos.quantidade.value;
		
		$.ajax({
			type:"PUT",
			url: SENAI.PATH + "produtos/alterar",
			data:JSON.stringify(cadProdutos),
			success: function(msg){
				Swal.fire(msg);
				SENAI.produtos.buscar();
				$("#modalEditaProdutos").dialog("close");
			},
			error: function(info){
				Swal.fire("Erro ao editar cadastro: "+ info.status+" - "+info.statusText);
			}
		});
	};
	SENAI.produtos.excluir = function(id){
		$.ajax({
			type:"DELETE",
			url: SENAI.PATH +"produtos/excluir/"+id,
			success: function(msg){
				Swal.fire(msg);
				SENAI.produtos.buscar();
			},
			error: function(info){
				Swal.fire("Erro ao excluir produto: " + info.status + " - " + info.statusText);
			}
		});
	};
})