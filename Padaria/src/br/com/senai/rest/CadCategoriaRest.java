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






@Path("cadCategoria")
public class CadCategoriaRest extends UtilRest{
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String cadCategoriaParam) {
		try {
			CadCategoria cadCategoria = new Gson().fromJson(cadCategoriaParam, CadCategoria.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCCadCategoriaDAO jdbcCadCategoria = new JDBCCadCategoriaDAO(conexao);
			boolean retorno  = jdbcCadCategoria.inserir(cadCategoria);
			String msg="";
			
			if(retorno) {
				msg = "Categoria cadastrado com sucesso!";
			}else {
				msg = "Erro ao cadastrar categoria.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/buscar")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String  nome) {
		try {
			List<JsonObject> listaCadCategoria = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCCadCategoriaDAO jdbcCadCategoria = new JDBCCadCategoriaDAO(conexao);
			listaCadCategoria = jdbcCadCategoria.buscarPorNome(nome);
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaCadCategoria);
			return this.buildResponse(json);
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
			CadCategoria cadCategoria= new CadCategoria();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCCadCategoriaDAO jdbcCadCategoria = new JDBCCadCategoriaDAO(conexao);

			cadCategoria = jdbcCadCategoria.checkId(id);

			conec.fecharConexao();
			return this.buildResponse(cadCategoria);

		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String cadCategoriaParam) {
		try {
			CadCategoria cadCategoria = new Gson().fromJson(cadCategoriaParam, CadCategoria.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCCadCategoriaDAO jdbcCadCategoria = new JDBCCadCategoriaDAO(conexao);

			boolean retorno = jdbcCadCategoria.alterar(cadCategoria);

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
			JDBCCadCategoriaDAO jdbcCadCategoria = new JDBCCadCategoriaDAO(conexao);
			
			boolean retorno = jdbcCadCategoria.deletar(id);
			
			String msg = "";
			if(retorno) {
				msg="Categoria exclu�da com sucesso!";
			}else {
				msg="Erro ao excluir categoria!";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}			
	}
	@GET
	@Path("/buscarSelCat")
	@Produces(MediaType.APPLICATION_JSON)

	public Response buscarSelCat() {

		try {
			List<CadCategoria> listaCategorias = new ArrayList<CadCategoria>();

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCCadCategoriaDAO jdbcCadCategoria = new JDBCCadCategoriaDAO(conexao);
			listaCategorias = jdbcCadCategoria.buscarSelCat();
			conec.fecharConexao();
			return this.buildResponse(listaCategorias);

		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}
