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
import br.com.senai.modelo.Produtos;
import br.com.senai.modelo.CadCategoria;

public class JDBCProdutosDAO implements ProdutosDAO{

	private Connection conexao;

	public JDBCProdutosDAO(Connection conexao) {
		this.conexao = conexao;
	}
	public boolean inserir (Produtos produtos) {
		String comando = "INSERT INTO produtos"
				+ "(idCategoria, nome, preco, quantidade) "
				+ "VALUES (?,?,?,?)";
		PreparedStatement p;

		try {

			p = this.conexao.prepareStatement(comando);			
			p.setInt(1,  produtos.getCategoriaId());
			p.setString(2, produtos.getNome());
			p.setFloat(3, produtos.getPreco());
			p.setInt(4, produtos.getQuantidade());
			p.execute();

		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	public List<JsonObject> buscar (String cliente){

		String comando = "SELECT *, categoria.nome as categoria FROM produtos "+
		"inner join categoria on categoria.idCategoria = produtos.idCategoria ";
		if (cliente != "") {
			comando += "WHERE produtos.nome LIKE '%"+ cliente + "%' OR produtos.idProdutos LIKE '%"+ cliente + "%' "; 
		}
		
		comando += "ORDER BY produtos.nome ASC";		
			List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
			JsonObject produtos = null;

			try {

				Statement stmt = conexao.createStatement();
				ResultSet rs = stmt.executeQuery(comando);

				while(rs.next()) {

					int idd = rs.getInt("idProdutos");
					String idCat = rs.getString("categoria");
					float preco = rs.getFloat("preco");
					String nome = rs.getString("nome");
					int quantidade = rs.getInt("quantidade");
				
					
					produtos = new JsonObject();
					produtos.addProperty("id", idd);
					produtos.addProperty("categoria", idCat);
					produtos.addProperty("preco", preco);
					produtos.addProperty("nome", nome);
					produtos.addProperty("quantidade", quantidade);
				
					
					
					listaProdutos.add(produtos);
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
		return listaProdutos;
	}
	public Produtos checkId(int id) {
		
		String comando = "SELECT * FROM produtos WHERE idProdutos = ?";
		Produtos produtos = new Produtos();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				int idd = rs.getInt("idProdutos");
				String nome = rs.getString("nome");	
				int idCat = rs.getInt("idCategoria");
				float preco = rs.getFloat("preco");
				int quantidade = rs.getInt("quantidade");
				produtos.setId(idd);
				produtos.setNome(nome);
				produtos.setCategoriaId(idCat);
				produtos.setPreco(preco);
				produtos.setQuantidade(quantidade);

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return produtos;
	}
		
		public boolean alterar(Produtos produtos) {		
			String comando = "UPDATE produtos "
					+ "SET nome=?, preco=?, idCategoria=?, quantidade=?"
					+ " WHERE idProdutos=?";
			PreparedStatement p;
			try {
				
				
				
				p = this.conexao.prepareStatement(comando);
				p.setString(1,produtos.getNome());
				p.setFloat(2,produtos.getPreco());
				p.setInt(3,produtos.getCategoriaId());
				p.setInt(4,produtos.getQuantidade());
				p.setInt(5,produtos.getId());

				p.executeUpdate();

			}catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	public boolean deletar(int id) {
		String comando = "DELETE FROM produtos WHERE idProdutos = ?";
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
}