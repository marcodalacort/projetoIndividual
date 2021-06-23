package br.com.senai.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.senai.jdbcinterface.ProdutosDAO;
import br.com.senai.jdbcinterface.VendasDAO;
import br.com.senai.modelo.Produtos;
import br.com.senai.modelo.Vendas;
import br.com.senai.modelo.CadCategoria;
import br.com.senai.modelo.Funcionario;

public class JDBCVendasDAO implements VendasDAO{

	private Connection conexao;

	public JDBCVendasDAO(Connection conexao) {
		this.conexao = conexao;
	}
	public boolean inserir (Vendas vendas) {
		String comando = "INSERT INTO vendas"
				+ "(data, idFuncionario) "
				+ "VALUES (?,?)";
		PreparedStatement p;

		try {

			p = this.conexao.prepareStatement(comando);			
			p.setString(1, vendas.getData());
			p.setInt(2, vendas.getIdFuncionario());
			p.execute();

		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	public boolean inserirProdVendas (Vendas vendas) {
		String comando = "INSERT INTO vendas_has_produtos"
				+ "(idVendas, idProdutos, quantidadeVenda, precoVenda) "
				+ "VALUES (?,?,?,?)";
		PreparedStatement p;

		try {

			p = this.conexao.prepareStatement(comando);			
			p.setInt(1,  vendas.getIdVendas());
			p.setInt(2, vendas.getIdProdutos());
			p.setInt(3, vendas.getQuantidadeVenda());
			p.setFloat(4, vendas.getValorVenda());
			p.execute();

		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	public List<JsonObject> buscar (String cliente){

		String comando = "SELECT * FROM vendas WHERE idVendas=(SELECT MAX(idVendas) FROM vendas)";
		
		
			
			List<JsonObject> listaVendas = new ArrayList<JsonObject>();
			JsonObject vendas = null;

			try {

				Statement stmt = conexao.createStatement();
				ResultSet rs = stmt.executeQuery(comando);

				while(rs.next()) {

					int idd = rs.getInt("idVendas");
					int idFunc = rs.getInt("idFuncionario");
				
					vendas = new JsonObject();
					vendas.addProperty("idVendas", idd);
					vendas.addProperty("idFuncionario", idFunc);
				
				
					
					
					listaVendas.add(vendas);
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
		return listaVendas;
	}
	public List<JsonObject> buscarProdVendas (String cliente){
		
		String comando = "SELECT *, produtos.nome as nome, produtos.preco as precoU, categoria.nome as categoria FROM vendas_has_produtos "+
						"INNER JOIN produtos on produtos.idProdutos = vendas_has_produtos.idProdutos "+
						"INNER JOIN categoria on categoria.idCategoria = produtos.idCategoria "+
							"WHERE idVendas=(SELECT MAX(idVendas) FROM vendas)";
		
				
						
					List<JsonObject> listaProdVenda = new ArrayList<JsonObject>();
					JsonObject produtosVenda = null;

					try {

						Statement stmt = conexao.createStatement();
						ResultSet rs = stmt.executeQuery(comando);

						while(rs.next()) {
							
							int idTab = rs.getInt("id");
							int idd = rs.getInt("idProdutos");
							String nome = rs.getString("nome");
							String idCat = rs.getString("categoria");
							int quantidade = rs.getInt("quantidadeVenda");
							float precoU = rs.getFloat("preco");
							float precoT = rs.getFloat("precoVenda");
							
						
							
							produtosVenda = new JsonObject();
							produtosVenda.addProperty("id", idTab);
							produtosVenda.addProperty("idProdutos", idd);
							produtosVenda.addProperty("nome", nome);
							produtosVenda.addProperty("categoria", idCat);
							produtosVenda.addProperty("quantidadeVenda", quantidade);
							produtosVenda.addProperty("precoUni", precoU);
							produtosVenda.addProperty("valorVenda", precoT);
						
							
							
							listaProdVenda.add(produtosVenda);
						}

					}catch (Exception e) {
						e.printStackTrace();
					}
				return listaProdVenda;
	}
	
	public List<JsonObject> buscarValorTotal (String cliente){

		String comando = "SELECT SUM(precoVenda) AS total FROM vendas_has_produtos WHERE idVendas=(SELECT MAX(idVendas) FROM vendas)";
		
			
			List<JsonObject> listaValorTotal = new ArrayList<JsonObject>();
			JsonObject valorTotal = null;

			try {

				Statement stmt = conexao.createStatement();
				ResultSet rs = stmt.executeQuery(comando);

				while(rs.next()) {

					float valorTotalVenda = rs.getFloat("total");
				
					valorTotal = new JsonObject();
					valorTotal.addProperty("valorTotalVenda", valorTotalVenda);
				
				
					
					
					listaValorTotal.add(valorTotal);
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
		return listaValorTotal;
	}
public Funcionario checkEmail(String email) {
		
		String comando = "SELECT * FROM funcionario WHERE email = ?";
		Funcionario funcionario = new Funcionario();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setString(1, email);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("idFuncionario");
				String nome = rs.getString("nome");
				String emailG = rs.getString("email");
				
				

				funcionario.setId(id);
				funcionario.setNome(nome);
				funcionario.setEmail(emailG);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return funcionario;
	}
	public Vendas checkId(int id) {
		
		String comando = "SELECT * FROM vendas_has_produtos WHERE id = ? AND idVendas=(SELECT MAX(idVendas) FROM vendas)";
		Vendas vendas = new Vendas();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				int idd = rs.getInt("idProdutos");
				int quantidade = rs.getInt("quantidadeVenda");
				float precoVenda = rs.getFloat("precoVenda");
				int idTab = rs.getInt("id");
				vendas.setIdProdutos(idd);
				vendas.setQuantidadeVenda(quantidade);
				vendas.setValorVenda(precoVenda);
				vendas.setId(idTab);

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return vendas;
	}
	
	public boolean alterar(Vendas vendas) {		
		String comando = "UPDATE vendas_has_produtos "
				+ "SET quantidadeVenda=?, precoVenda=?"
				+ " WHERE id=?";
		PreparedStatement p;
		try {
			
			
			p = this.conexao.prepareStatement(comando);
			p.setInt(1,vendas.getQuantidadeVenda());
			p.setFloat(2, vendas.getValorVenda());
			p.setInt(3,vendas.getId());
			

			p.executeUpdate();

		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean alterarIdFuncio(Vendas vendas) {		
		String comando = "update vendas v join (select max(v2.idVendas) as idMax  from vendas v2) as sub on v.idVendas = sub.idMax set v.idFuncionario=?, v.data=?";
		PreparedStatement p;
		try {
			
			
			p = this.conexao.prepareStatement(comando);
			p.setInt(1,vendas.getIdFuncionario());
			p.setNString(2, vendas.getData());
		
			p.executeUpdate();

		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deletar(int id) {
		String comando = "DELETE FROM vendas_has_produtos WHERE id = ?";
		PreparedStatement p;
		try {
			p=this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deletarVendas() {
		String comando = "DELETE FROM vendas WHERE idVendas=(SELECT MAX(idVendas) FROM vendas_has_produtos)";
		PreparedStatement p;
		try {
			p=this.conexao.prepareStatement(comando);
			p.execute();
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}