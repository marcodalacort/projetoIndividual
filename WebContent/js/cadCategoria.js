
SENAI.cadCategoria = new Object();

$(document).ready(function(){

	SENAI.PATH = "/Senai/rest/";
	SENAI.cadCategoria.cadastrar = function(){


		var cadCategoria = new Object();
		cadCategoria.nome = document.frmAddCadCategoria.categoria.value;

		$.ajax({
			type: "POST",
			url: SENAI.PATH + "cadCategoria/inserir",
			data:JSON.stringify(cadCategoria),
			success:function(msg){
				Swal.fire(msg);
				$("#addCadCategoria").trigger("reset");
				SENAI.cadCategoria.buscar();
			},
			error:function(info){
				Swal.fire("Erro ao cadastrar uma nova categoria: "+ info.status + " - "+ info.statusText);	
			}
		});	
	};
	SENAI.cadCategoria.buscar = function(){
		var valorBusca = $("#campoBusca").val();
		$.ajax({
			type: "GET",
			url: SENAI.PATH + "cadCategoria/buscar",
			data: "valorBusca="+valorBusca,
			success: function(dados){

				dados = JSON.parse(dados);
				
				var cabecalho = 
				"<table>"+
				"<tr>"+	
				"<th class='nome'> Nome</th>"+
				"<th class='acoes'>Editar</th>"+
				"</tr>"+
				"</table>";
				
				$("#listaCabCat").html(cabecalho);
				$("#listaCadCategoria").html(SENAI.cadCategoria.exibir(dados));
				$(document).ajaxComplete(function () {
        			paginate('#listaCadCategoria',4);
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
				SENAI.exibirAviso("Erro ao consultar os cadastros de categoria: "+info.status+" - "+info.statusText);
			}
		});

		SENAI.cadCategoria.exibir = function(listaDeCadCategorias){
			var tabela = 
				"<table>";

			if(listaDeCadCategorias != undefined && listaDeCadCategorias.length >0){


				for(var i=0; i<listaDeCadCategorias.length; i++){

					tabela+="<tr>"+	
					"<td class='nome'>"+listaDeCadCategorias[i].nome+"</td>"+
					"<td class='acoes'>"+
					"<a onclick=\"SENAI.cadCategoria.exibirEdicao('"+listaDeCadCategorias[i].id+"')\"><img src='css/img/edit.png' alt='Editar'></a>" +
					"<a onclick=\"SENAI.cadCategoria.excluir('"+listaDeCadCategorias[i].id+"')\"><img src='css/img/delete.png' alt='Apagar'></a>" +
					"</td>"+
					"</tr>";
				}

			}else if (listaDeCadCategorias == ""){
				tabela += "<tr><td colspan='6'>Nenhum registro encontrado</td></tr>";
			}
			tabela +="</table>";

			return tabela;

			$("#listaCadCategorias").html(tabela);
		}
	}
	SENAI.cadCategoria.buscar();
	SENAI.cadCategoria.exibirEdicao = function(id){
		$.ajax({
			type:"GET",
			url: SENAI.PATH +"cadCategoria/checkId",
			data: "id="+id,
			success: function(cadCategoria){
					document.frmEditaCadCategoria.idCadCategoria.value=cadCategoria.id;			
					document.frmEditaCadCategoria.categoria.value = cadCategoria.nome;
					
					var modalEditaCadCategoria = {
							title: "Editar Categorias",
							height: 200,
							width: 400,
							modal: true,
							buttons:{
								"Salvar":function(){
									
									SENAI.cadCategoria.editar();							
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
					$("#modalEditaCadCategoria").dialog(modalEditaCadCategoria);
		
			},
			error: function(info){
				Swal.fire("Erro ao buscar cadastro para edição: "+info.status+" - "+info.statusText);
			}

		});
	}
	
	SENAI.cadCategoria.editar = function(){
		 
		
		var cadCategoria = new Object();
		cadCategoria.id =document.frmEditaCadCategoria.idCadCategoria.value;
		cadCategoria.nome=document.frmEditaCadCategoria.categoria.value;
		
		$.ajax({
			type:"PUT",
			url: SENAI.PATH + "cadCategoria/alterar",
			data:JSON.stringify(cadCategoria),
			success: function(msg){
				Swal.fire(msg);
				SENAI.cadCategoria.buscar();
				$("#modalEditaCadCategoria").dialog("close");
			},
			error: function(info){
				Swal.fire("Erro ao editar cadastro: "+ info.status+" - "+info.statusText);
			}
		});
	};
	SENAI.cadCategoria.excluir = function(id){
		$.ajax({
			type:"DELETE",
			url: SENAI.PATH +"cadCategoria/excluir/"+id,
			success: function(msg){
				Swal.fire(msg);
				SENAI.cadCategoria.buscar();
			},
			error: function(info){
				Swal.fire("Erro ao excluir categoria: " + info.status + " - " + info.statusText);
			}
		});
	};
})
