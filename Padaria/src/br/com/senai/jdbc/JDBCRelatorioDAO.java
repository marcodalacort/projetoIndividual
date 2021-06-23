package br.com.senai.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.JsonObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import br.com.senai.jdbcinterface.FuncionarioDAO;
import br.com.senai.jdbcinterface.RelatorioDAO;
import br.com.senai.modelo.CadCategoria;
import br.com.senai.modelo.Cargos;
import br.com.senai.modelo.Funcionario;
import br.com.senai.modelo.Relatorio;



public class JDBCRelatorioDAO implements RelatorioDAO{
	
private Connection conexao;

public JDBCRelatorioDAO(Connection conexao) {
	this.conexao = conexao;
}

public List<JsonObject> buscarData (String dataIni, String dataFin){

	String comando = "SELECT vendas.idVendas as idVenda, group_concat(produtos.nome SEPARATOR ' </br> ') as nomeProduto, group_concat(Concat('R$ ',Replace (Replace (Replace (Format(produtos.preco, 2), '.', '|'), ',', '.'), '|', ',')) SEPARATOR ' </br> ') as valorUnitario, "
			+ "group_concat( vendas_has_produtos.quantidadeVenda SEPARATOR ' </br> ') as quantidade, group_concat(Concat('R$ ',Replace (Replace (Replace (Format(vendas_has_produtos.precoVenda, 2), '.', '|'), ',', '.'), '|', ',')) SEPARATOR ' </br> ') as precoParcial, "
			+ "Concat('R$ ',Replace (Replace (Replace (Format(sum(precoVenda), 2), '.', '|'), ',', '.'), '|', ',')) as precoTotal, funcionario.nome as nomeFuncionario, vendas.data as data FROM vendas_has_produtos "+
			"INNER JOIN vendas on vendas.idVendas = vendas_has_produtos.idVendas "+
			"INNER JOIN funcionario on funcionario.idFuncionario = vendas.idFuncionario " + 
			"INNER JOIN produtos on produtos.idProdutos = vendas_has_produtos.idProdutos ";
	if (dataIni != "" && dataFin != "") {
		comando += "WHERE vendas.data >= '" + dataIni + "' AND vendas.data <= '" + dataFin + "' ";
		
	}

	comando += "group by idVenda order by idVenda";		
		List<JsonObject> listaVendas = new ArrayList<JsonObject>();
		JsonObject vendas = null;

		try {

			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);

			while(rs.next()) {

				int id = rs.getInt("idVenda");
				String nomeProduto = rs.getString("nomeProduto");
				String nomeFuncionario = rs.getString("nomeFuncionario");
				String data = rs.getString("data");
				String precoU = rs.getString("valorUnitario");
				String precoP = rs.getString("precoParcial");
				String precoT = rs.getString("precoTotal");
				String quantidade = rs.getString("quantidade");
				
				vendas = new JsonObject();
				vendas.addProperty("idVenda", id);
				vendas.addProperty("nomeProduto", nomeProduto);
				vendas.addProperty("nomeFuncionario", nomeFuncionario);
				vendas.addProperty("data", data);
				vendas.addProperty("valorUnitario", precoU);
				vendas.addProperty("precoParcial", precoP);
				vendas.addProperty("precoTotal", precoT);
				vendas.addProperty("quantidade", quantidade);
				
				
				
				listaVendas.add(vendas);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		return listaVendas;
	}

public List<JsonObject> buscar (String campoBusca){
	String comando = "SELECT vendas.idVendas as idVenda, group_concat(produtos.nome SEPARATOR ' </br> ') as nomeProduto, group_concat(Concat('R$ ',Replace (Replace (Replace (Format(produtos.preco, 2), '.', '|'), ',', '.'), '|', ',')) SEPARATOR ' </br> ') as valorUnitario, "
			+ "group_concat( vendas_has_produtos.quantidadeVenda SEPARATOR ' </br> ') as quantidade, group_concat(Concat('R$ ',Replace (Replace (Replace (Format(vendas_has_produtos.precoVenda, 2), '.', '|'), ',', '.'), '|', ',')) SEPARATOR ' </br> ') as precoParcial, "
			+ "Concat('R$ ',Replace (Replace (Replace (Format(sum(precoVenda), 2), '.', '|'), ',', '.'), '|', ',')) as precoTotal, funcionario.nome as nomeFuncionario, vendas.data as data FROM vendas_has_produtos "+
			"INNER JOIN vendas on vendas.idVendas = vendas_has_produtos.idVendas "+
			"INNER JOIN funcionario on funcionario.idFuncionario = vendas.idFuncionario " + 
			"INNER JOIN produtos on produtos.idProdutos = vendas_has_produtos.idProdutos ";
			if (campoBusca != "") {
				comando += "WHERE vendas.idVendas LIKE '%" + campoBusca + "%' OR vendas.idVendas LIKE '%" + campoBusca + "%' ";
			}
	comando += "group by idVenda order by idVenda";		
		List<JsonObject> listaVendas = new ArrayList<JsonObject>();
		JsonObject vendas = null;
		try {

			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);

			while(rs.next()) {

				int id = rs.getInt("idVenda");
				String nomeProduto = rs.getString("nomeProduto");
				String nomeFuncionario = rs.getString("nomeFuncionario");
				String data = rs.getString("data");
				String precoU = rs.getString("valorUnitario");
				String precoP = rs.getString("precoParcial");
				String precoT = rs.getString("precoTotal");
				String quantidade = rs.getString("quantidade");
				
				
				vendas = new JsonObject();
				vendas.addProperty("idVenda", id);
				vendas.addProperty("nomeProduto", nomeProduto);
				vendas.addProperty("nomeFuncionario", nomeFuncionario);
				vendas.addProperty("data", data);
				vendas.addProperty("valorUnitario", precoU);
				vendas.addProperty("precoParcial", precoP);
				vendas.addProperty("precoTotal", precoT);
				vendas.addProperty("quantidade", quantidade);
				
				
				
				listaVendas.add(vendas);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		return listaVendas;
	}

}
