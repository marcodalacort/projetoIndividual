SENAI = new Object();

$(document).ready(function() {
	
	//Função para carregamento de páginas de conteúdo, que
	//recebe como parâmetro o nome da pasta com a página a ser carregada
	SENAI.carregaPagina = function(pagename){
		//Remove o conteúdo criado na abertura de uma janela modal pelo JQueryUI
		if($(".ui-dialog"))
			$(".ui-dialog").remove();
		
		//Limpa a tag section, excluindo todoo conteúdo de dentro dela
		$("section").empty();
		//Carrega a página solicitada dentro da tag section
		$("section").load(pagename+"/", function(response,status,info)
				{
			if (status == "error"){
				var msg = "Houve um erro ao encontrar a página: "+info.status + " - " + info.statusText;
				$("section").html(msg);
			}
				});
	}
	//Exibe os valores financeiros no formato da moeda Real
	SENAI.formatarDinheiro = function(valor){
		return valor.toFixed(2).replace('.',',').replace(/(\d)(?=(\d{3})+\,)/g, "$1.");
	}
	
	//Define as configurações base de uma modal de aviso
	SENAI.exibirAviso = function(aviso){
		var modal = {
		title: "Mensagem",
		height: 250,
		width: 400,
		modal:true,
		buttons: {
			"OK": function(){
				$(this).dialog("close");
			}
		}
		};
		$("#modalAviso").html(aviso);
		$("#modalAviso").dialog(modal);
		
	}
	
	
});
