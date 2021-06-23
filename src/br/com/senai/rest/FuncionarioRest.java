package br.com.senai.rest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import br.com.senai.bd.Conexao;
import br.com.senai.jdbc.JDBCCadCategoriaDAO;
import br.com.senai.jdbc.JDBCFuncionarioDAO;
import br.com.senai.modelo.CadCategoria;
import br.com.senai.modelo.Cargos;
import br.com.senai.modelo.Funcionario;




@Path("funcionario")
public class FuncionarioRest extends UtilRest{
	
	@GET
	@Path("/buscar")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorMatricula(@QueryParam("valorBusca") String  matricula) {
		try {
			List<JsonObject> listaFuncionarios = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
			listaFuncionarios = jdbcFuncionario.buscarPorMatricula(matricula);
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaFuncionarios);
			return this.buildResponse(json);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}				
	}
	
	
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String funcionarioParam) {
		try {
			Funcionario funcionario = new Gson().fromJson(funcionarioParam, Funcionario.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
			boolean retorno  = jdbcFuncionario.inserir(funcionario);
			String msg="";
			
			if(retorno) {
				msg = "Funcionario cadastrado com sucesso!";
			}else {
				msg = "Erro ao cadastrar Funcionario.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/checkId")
	@Produces(MediaType.APPLICATION_JSON)

	public Response checkId(@QueryParam("id")int id) {

		try {
			Funcionario funcionario= new Funcionario();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);

			funcionario = jdbcFuncionario.checkId(id);

			conec.fecharConexao();
			return this.buildResponse(funcionario);

		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String funcionarioParam) {
		try {
			Funcionario funcionario = new Gson().fromJson(funcionarioParam, Funcionario.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);

			boolean retorno = jdbcFuncionario.alterar(funcionario);

			String msg="";
			if (retorno) {
				msg = "Cadastro alterado com sucesso!";
			}else {
				msg = "Erro ao alterar cadastro";
			}
			conec.fecharConexao();
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/excluir/{id}")
	@Consumes("application/*")
	public Response excluir(@PathParam("id") int id) {
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
			
			boolean retorno = jdbcFuncionario.deletar(id);
			
			String msg = "";
			if(retorno) {
				msg="Funcionario exclu�do com sucesso!";
			}else {
				msg="Erro ao excluir produto!";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}			
	}
	@GET
	@Path("/checkIdSenha")
	@Produces(MediaType.APPLICATION_JSON)

	public Response checkIdSenha(@QueryParam("id")int id) {

		try {
			Funcionario funcionario= new Funcionario();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);

			funcionario = jdbcFuncionario.checkIdSenha(id);

			conec.fecharConexao();
			return this.buildResponse(funcionario);

		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@PUT
	@Path("/alterarSenha")
	@Consumes("application/*")
	public Response alterarSenha(String funcionarioParam) {
		try {
			Funcionario funcionario = new Gson().fromJson(funcionarioParam, Funcionario.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);

			boolean retorno = jdbcFuncionario.alterarSenha(funcionario);

			String msg="";
			if (retorno) {
				msg = "Senha atualizada com sucesso!";
			}else {
				msg = "Erro ao atualizar senha";
			}
			conec.fecharConexao();
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@GET
	@Path("/buscarSelO")
	@Produces(MediaType.APPLICATION_JSON)

	public Response buscarSelO() {

		try {
			List<Funcionario> listaFuncionario = new ArrayList<Funcionario>();

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
			listaFuncionario = jdbcFuncionario.buscarSelO();
			conec.fecharConexao();
			return this.buildResponse(listaFuncionario);

		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@GET
	@Path("/buscarSelC")
	@Produces(MediaType.APPLICATION_JSON)

	public Response buscarSelC() {

		try {
			List<Cargos> listaCargos = new ArrayList<Cargos>();

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
			listaCargos = jdbcFuncionario.buscarSelC();
			conec.fecharConexao();
			return this.buildResponse(listaCargos);

		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}