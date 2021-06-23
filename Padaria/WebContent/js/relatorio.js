

SENAI.relatorio = new Object();

$(document).ready(function(){

	SENAI.PATH = "/Senai/rest/"

	SENAI.relatorio.formatDate = function(input) {
		var datePart = input.match(/\d+/g),
		ano = datePart[0].substring(0), //0 = 4 digitos e 2 = 2 digitos
		mes = datePart[1], 
		dia = datePart[2], 
		hora = datePart[3], 
		minuto = datePart[4]
		segundo = datePart[5];

		return dia+'/'+mes+'/'+ano+' '+hora+':'+minuto+':'+segundo;
	}
	
	SENAI.relatorio.formatDateR = function(input) {
		var datePart = input.match(/\d+/g),
		ano = datePart[0].substring(0), //0 = 4 digitos e 2 = 2 digitos
		mes = datePart[1], 
		dia = datePart[2], 
		hora = datePart[3], 
		minuto = datePart[4]
		segundo = datePart[5];

		return dia+'/'+mes+'/'+ano+' '+hora+':'+minuto;
	}
	
	SENAI.relatorio.gerarPDF = function(){
		var valorDataIni = $(".dataI").val();
		var valorDataFin = $(".dataF").val();
		if(valorDataIni > valorDataFin){
			Swal.fire({
				icon: 'error',
				title: 'Erro!',
				text: 'Primeira data não pode ser maior que a segunda data!'
			})
		} else if(valorDataIni == '' || valorDataFin == ''){
			Swal.fire({
				icon: 'error',
				title: 'Erro!',
				text: 'Data não selecionada!'
			})
		}else{

			$.ajax({
				type: "GET",
				url: SENAI.PATH + "relatorio/buscarData",
				data: {valorDataIni: valorDataIni, valorDataFin: valorDataFin},
				success: function(dados){

					dados = JSON.parse(dados);

					//$("#listaRelatorio").html(SENAI.relatorio.exibir(dados));
					
					var style = "<style>";
        			style = style + "table {width: 100%;font: 20px Calibri;}";
        			style = style + "table, th, td {border: solid 1px #DDD; border-collapse: collapse;";
        			style = style + "padding: 2px 3px;text-align: center;}";
        			style = style + "</style>";
        			// CRIA UM OBJETO WINDOW
        			var win = window.open('', '', 'height=700,width=700');
        			win.document.write('<html><head>');
        			win.document.write('<title>Relatório Vendas por Data de '+SENAI.relatorio.formatDateR(valorDataIni)+' até '+SENAI.relatorio.formatDateR(valorDataFin)+'</title>');   // <title> CABEÇALHO DO PDF.
        			win.document.write(style);                                     // INCLUI UM ESTILO NA TAB HEAD
        			win.document.write('</head>');
        			win.document.write('<body>');
        			win.document.write(SENAI.relatorio.exibir(dados));                          // O CONTEUDO DA TABELA DENTRO DA TAG BODY
        			win.document.write('</body></html>');
        			win.document.close(); 	                                         // FECHA A JANELA
        			win.print();                                                            // IMPRIME O CONTEUDO


				},
				error: function(info){
					Swal.fire("Erro ao consultar os cadastros de producao: "+info.status+" - "+info.statusText);
				}
			});
		}
	}	

	SENAI.relatorio.buscarData = function(){
		var valorDataIni = $(".dataI").val();
		var valorDataFin = $(".dataF").val();
		if(valorDataIni > valorDataFin){
			Swal.fire({
				icon: 'error',
				title: 'Erro!',
				text: 'Primeira data não pode ser maior que a segunda data!'
			})
		}else{

			$.ajax({
				type: "GET",
				url: SENAI.PATH + "relatorio/buscarData",
				data: {valorDataIni: valorDataIni, valorDataFin: valorDataFin},
				success: function(dados){

					dados = JSON.parse(dados);

					$("#listaRelatorio").html(SENAI.relatorio.exibir(dados));


				},
				error: function(info){
					Swal.fire("Erro ao consultar os cadastros de producao: "+info.status+" - "+info.statusText);
				}
			});
		}
	}	

	SENAI.relatorio.buscar = function(){
		var valorBusca = $("#campoBusca").val();
		var valorDataIni = $(".dataI").val();
		var valorDataFin = $(".dataF").val();

		$.ajax({
			type: "GET",
			url: SENAI.PATH + "relatorio/buscar",
			data: "valorBusca="+valorBusca,
			success: function(dados){
				
				
				dados = JSON.parse(dados);
				
				$("#listaRelatorio").html(SENAI.relatorio.exibir(dados));


			},
			error: function(info){
				Swal.fire("Erro ao consultar as vendas: "+info.status+" - "+info.statusText);
			}
		});

		SENAI.relatorio.exibir = function(listaDeVendas){

			var tabela = 
				"<table>"+
				"<tr>"+	
				"<th> ID Venda</th>"+
				"<th> Produtos</th>"+
				"<th> Valor Unitario</th>"+
				"<th> Quantidade</th>"+
				"<th> Preço Parcial</th>"+
				"<th> Preço Total</th>"+
				"<th> Nome Funcionario</th>"+
				"<th> Data</th>"+
				"</tr>";

			if(listaDeVendas != undefined && listaDeVendas.length >0){


				for(var i=0; i<listaDeVendas.length; i++){

					tabela+="<tr>"+	
					"<td>"+listaDeVendas[i].idVenda+"</td>"+
					"<td>"+listaDeVendas[i].nomeProduto+"</td>"+
					"<td>"+listaDeVendas[i].valorUnitario+"</td>"+
					"<td>"+listaDeVendas[i].quantidade+"</td>"+
					"<td>"+listaDeVendas[i].precoParcial+"</td>"+
					"<td>"+listaDeVendas[i].precoTotal+"</td>"+
					"<td>"+listaDeVendas[i].nomeFuncionario+"</td>"+
					"<td>"+SENAI.relatorio.formatDate(listaDeVendas[i].data)+"</td>"+
					"</tr>";
				}

			}else if (listaDeVendas == ""){
				tabela += "<tr><td colspan='12'>Nenhum registro encontrado</td></tr>";
			}
			tabela +="</table>";

			return tabela;

		
		}
		
	}
	SENAI.relatorio.buscar();
	
	
	
	
	
		
		
	
	
	
	
		
	
	
})