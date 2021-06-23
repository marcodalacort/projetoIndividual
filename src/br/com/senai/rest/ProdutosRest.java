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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.senai.bd.Conexao;
import br.com.senai.jdbc.JDBCCadCategoriaDAO;
import br.com.senai.jdbc.JDBCProdutosDAO;
import br.com.senai.modelo.CadCategoria;
import br.com.senai.modelo.Produtos;

@Path("produtos")
public class ProdutosRest extends UtilRest{
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String produtosParam) {
		try {
			Produtos produtos = new Gson().fromJson(produtosParam, Produtos.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCProdutosDAO jdbcProdutos = new JDBCProdutosDAO(conexao);
			boolean retorno  = jdbcProdutos.inserir(produtos);
			String msg="";
			
			if(retorno) {
				msg = "Produto cadastrado com sucesso!";
			}else {
				msg = "Erro ao cadastrar produto.";
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
	public Response buscar(@QueryParam("valorBusca") String  cliente) {
		try {
			List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutosDAO jdbcProdutos = new JDBCProdutosDAO(conexao);
			listaProdutos = jdbcProdutos.buscar(cliente);
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaProdutos);
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
			Produtos produtos= new Produtos();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutosDAO jdbcProdutos = new JDBCProdutosDAO(conexao);

			produtos = jdbcProdutos.checkId(id);

			conec.fecharConexao();
			return this.buildResponse(produtos);

		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String produtosParam) {
		try {
			Produtos produtos = new Gson().fromJson(produtosParam, Produtos.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutosDAO jdbcProdutos = new JDBCProdutosDAO(conexao);

			boolean retorno = jdbcProdutos.alterar(produtos);

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
			JDBCProdutosDAO jdbcProdutos = new JDBCProdutosDAO(conexao);
			
			boolean retorno = jdbcProdutos.deletar(id);
			
			String msg = "";
			if(retorno) {
				msg="Produto excluido com sucesso!";
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
}
