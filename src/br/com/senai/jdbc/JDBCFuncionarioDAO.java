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
import br.com.senai.modelo.CadCategoria;
import br.com.senai.modelo.Cargos;
import br.com.senai.modelo.Funcionario;



public class JDBCFuncionarioDAO implements FuncionarioDAO{
	
private Connection conexao;

public JDBCFuncionarioDAO(Connection conexao) {
	this.conexao = conexao;
}
public List<JsonObject> buscarPorMatricula (String matricula){

	//Inicia a cria��o do comando SQL de busca 
	String comando = "SELECT *, cargo.nome as cargo FROM funcionario "+
			"inner join cargo on cargo.idCargo = funcionario.idCargo ";
	//Se o nome n�o estiver vazio...
	if (matricula != "") {
		//concatena no comando o WHERE buscando no nome do produto 
		//o texto da vari�vel nome
		comando += "WHERE funcionario.nome LIKE '%"+ matricula + "%' "; 
	}
	//Finaliza o comando ordenando alfabeticamente por
	//categoria, marca e depois modelo
	comando += "ORDER BY funcionario.nome ASC";
	
	
		List<JsonObject> listaFuncionarios = new ArrayList<JsonObject>();
		JsonObject funcionario = null;

		try {

			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);

			while(rs.next()) {

				int id = rs.getInt("idFuncionario");
				String cargo = rs.getString("cargo");
				String nome = rs.getString("nome");
				String senha = rs.getString("senha");
				String email = rs.getString("email");
				
				funcionario = new JsonObject();
				funcionario.addProperty("id", id);
				funcionario.addProperty("cargo", cargo);
				funcionario.addProperty("nome", nome);
				funcionario.addProperty("senha", senha);
				funcionario.addProperty("email", email);
				
				listaFuncionarios.add(funcionario);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		return listaFuncionarios;
	}

public Funcionario checkId(int id) {
	System.out.println(id);
	
	String comando = "SELECT * FROM funcionario WHERE idFuncionario = ?";
	Funcionario funcionario = new Funcionario();
	try {
		PreparedStatement p = this.conexao.prepareStatement(comando);
		p.setInt(1, id);
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			int idd = rs.getInt("idFuncionario");
			int cargo = rs.getInt("idCargo");
			String nome = rs.getString("nome");
			String senha = rs.getString("senha");
			String email = rs.getString("email");
			funcionario.setId(idd);
			funcionario.setCargo(cargo);
			funcionario.setNome(nome);
			funcionario.setSenha(senha);
			funcionario.setEmail(email);

		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return funcionario;
}
	
	public boolean alterar(Funcionario funcionario) {		
		String comando = "UPDATE funcionario "
				+ "SET idCargo=?, nome=?, email=?"
				+ " WHERE idFuncionario=?";
		PreparedStatement p;
		try {
			
			
			
			p = this.conexao.prepareStatement(comando);
			p.setInt(1,funcionario.getCargo());
			p.setString(2,funcionario.getNome());
			p.setString(3,funcionario.getEmail());
			p.setInt(4,funcionario.getId());

			p.executeUpdate();

		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
public boolean inserir (Funcionario funcionario) {
	
		String comando = "INSERT INTO funcionario "
				+ "(idCargo, nome, senha, email) "
				+ "VALUES (?,?,?,?)";
		PreparedStatement p;
		
		try {
			//Prepara o comando para execução no BD em que nos conectamos
			
			p = this.conexao.prepareStatement(comando);
			
			//Substitui no comando os "?" pelos valores do produto
			
			
			String salt="DGE$5SGr@3VsHYUMas2323E4d57vfBfFSTRU@!DSH(*%FDSdfg13sgfsg";
			String senhaSalt = funcionario.getSenha()+salt;
			String senhaSha1SemSal = DigestUtils.shaHex(senhaSalt);
			
			p.setInt(1, funcionario.getCargo());
			p.setString(2, funcionario.getNome());
			p.setString(3, senhaSha1SemSal);
			p.setString(4, funcionario.getEmail());
			
			
			//Executa o comando no BD
			p.execute();
			
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
public boolean deletar(int id) {
	String comando = "DELETE FROM funcionario WHERE idFuncionario = ?";
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
public Funcionario checkIdSenha(int id) {
	
	String comando = "SELECT * FROM funcionario WHERE idFuncionario = ?";
	Funcionario funcionario = new Funcionario();
	try {
		PreparedStatement p = this.conexao.prepareStatement(comando);
		p.setInt(1, id);
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			int iduser = rs.getInt("idFuncionario");			
			String senha = rs.getString("senha");	
			funcionario.setId(iduser);			
			funcionario.setSenha(senha);
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return funcionario;
}
public boolean alterarSenha(Funcionario funcionario) {	
	System.out.println(funcionario.getSenha());
	String comando = "UPDATE funcionario "
			+ "SET senha=?"
			+ " WHERE idFuncionario=?";
	PreparedStatement p;
	try {
		
		String salt="DGE$5SGr@3VsHYUMas2323E4d57vfBfFSTRU@!DSH(*%FDSdfg13sgfsg";
		String senhaSalt = funcionario.getSenha()+salt;
		String senhaSha1ComSal = DigestUtils.shaHex(senhaSalt);
		
		p = this.conexao.prepareStatement(comando);
		p.setString(1,senhaSha1ComSal);
		p.setInt(2,funcionario.getId());

		p.executeUpdate();

	}catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}
public List<Funcionario> buscarSelO (){

	String comando = "SELECT * FROM funcionario";
	List<Funcionario> listaFuncionario = new ArrayList<Funcionario>();
	Funcionario funcionario = null;

	try {

		Statement stmt = conexao.createStatement();

		ResultSet rs = stmt.executeQuery(comando);

		while(rs.next()) {

			funcionario = new Funcionario();

			int idd = rs.getInt("idFuncionario");
			String nome = rs.getString("nome");
			

			funcionario.setId(idd);
			funcionario.setNome(nome);
			listaFuncionario.add(funcionario);
		}

	}catch (Exception ex) {

		ex.printStackTrace();
	}
	return listaFuncionario;
}
public List<Cargos> buscarSelC (){

	String comando = "SELECT * FROM cargo";
	List<Cargos> listaCargos = new ArrayList<Cargos>();
	Cargos cargo = null;

	try {

		Statement stmt = conexao.createStatement();

		ResultSet rs = stmt.executeQuery(comando);

		while(rs.next()) {

			cargo = new Cargos();

			int idd = rs.getInt("idCargo");
			String nome = rs.getString("nome");
			

			cargo.setId(idd);
			cargo.setNome(nome);
			listaCargos.add(cargo);
		}

	}catch (Exception ex) {

		ex.printStackTrace();
	}
	return listaCargos;
}
}
