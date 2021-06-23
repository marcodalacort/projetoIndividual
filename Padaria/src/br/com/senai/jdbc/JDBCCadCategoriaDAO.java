package br.com.senai.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.senai.jdbcinterface.CadCategoriaDAO;
import br.com.senai.modelo.CadCategoria;


public class JDBCCadCategoriaDAO implements CadCategoriaDAO{
	
private Connection conexao;

public JDBCCadCategoriaDAO(Connection conexao) {
	this.conexao = conexao;
}
	public boolean inserir (CadCategoria cadCategoria) {
		
		String comando = "INSERT INTO categoria"
				+ "(nome) "
				+ "VALUES (?)";
		PreparedStatement p;
		
		try {
			
			p = this.conexao.prepareStatement(comando);			
			p.setString(1, cadCategoria.getNome());
			p.execute();
			
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public List<JsonObject> buscarPorNome (String nome){

		//Inicia a cria��o do comando SQL de busca 
		String comando = "SELECT * FROM categoria ";
		//Se o nome n�o estiver vazio...
		if (nome != "") {
			//concatena no comando o WHERE buscando no nome do produto 
			//o texto da vari�vel nome
			comando += "WHERE categoria.nome LIKE '%"+ nome + "%' "; 
		}
		//Finaliza o comando ordenando alfabeticamente por
		//categoria, marca e depois modelo
		comando += "ORDER BY categoria.nome ASC";
		
		
			List<JsonObject> listaCadCategorias = new ArrayList<JsonObject>();
			JsonObject cadCategoria = null;

			try {

				Statement stmt = conexao.createStatement();
				ResultSet rs = stmt.executeQuery(comando);

				while(rs.next()) {

					int id = rs.getInt("idCategoria");
					String nomeCad = rs.getString("nome");
					
					cadCategoria = new JsonObject();
					cadCategoria.addProperty("id", id);
					cadCategoria.addProperty("nome", nomeCad);
					
					
					listaCadCategorias.add(cadCategoria);
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
			return listaCadCategorias;
		}
	public CadCategoria checkId(int id) {
		
		String comando = "SELECT * FROM categoria WHERE idCategoria = ?";
		CadCategoria cadCategoria = new CadCategoria();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				int idd = rs.getInt("idCategoria");
				String nome = rs.getString("nome");				
				cadCategoria.setId(idd);
				cadCategoria.setNome(nome);

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return cadCategoria;
	}
		
		public boolean alterar(CadCategoria cadCategoria) {		
			String comando = "UPDATE categoria "
					+ "SET nome=?"
					+ " WHERE idCategoria=?";
			PreparedStatement p;
			try {
				
				
				
				p = this.conexao.prepareStatement(comando);
				p.setString(1,cadCategoria.getNome());
				p.setInt(2,cadCategoria.getId());

				p.executeUpdate();

			}catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		public boolean deletar(int id) {
			String comando = "DELETE FROM categoria WHERE idCategoria = ?";
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
		public List<CadCategoria> buscarSelCat (){

			String comando = "SELECT * FROM categoria";
			List<CadCategoria> listaCadCategorias = new ArrayList<CadCategoria>();
			CadCategoria cadCategoria = null;

			try {

				Statement stmt = conexao.createStatement();

				ResultSet rs = stmt.executeQuery(comando);

				while(rs.next()) {

					cadCategoria = new CadCategoria();

					int idd = rs.getInt("idCategoria");
					String nome = rs.getString("nome");
					

					cadCategoria.setId(idd);
					cadCategoria.setNome(nome);
					listaCadCategorias.add(cadCategoria);
				}

			}catch (Exception ex) {

				ex.printStackTrace();
			}
			return listaCadCategorias;
		}
}