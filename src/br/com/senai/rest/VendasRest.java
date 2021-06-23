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
import br.com.senai.jdbc.JDBCVendasDAO;
import br.com.senai.modelo.CadCategoria;
import br.com.senai.modelo.Funcionario;
import br.com.senai.modelo.Produtos;
import br.com.senai.modelo.Vendas;

@Path("vendas")
public class VendasRest extends UtilRest{
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String vendasParam) {
		try {
			Vendas vendas = new Gson().fromJson(vendasParam, Vendas.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);
			boolean retorno  = jdbcVendas.inserir(vendas);
			String msg="";
			
			if(retorno) {
				msg = "Venda cadastrada com sucesso!";
			}else {
				msg = "Erro ao cadastrar venda.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@POST
	@Path("/inserirProdVendas")
	@Consumes("application/*")
	public Response inserirProdVendas(String vendasParam) {
		try {
			Vendas vendasProdutos = new Gson().fromJson(vendasParam, Vendas.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);
			boolean retorno  = jdbcVendas.inserirProdVendas(vendasProdutos);
			String msg="";
			
			if(retorno) {
				msg = "Produto na Venda cadastrada com sucesso!";
			}else {
				msg = "Produto já está adicionado na venda. Edite se quiser adicionar a mais!";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@GET
	@Path("/checkEmail")
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response checkEmail(@QueryParam("email")String email) {

		try {
			Funcionario funcionario = new Funcionario();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);
			funcionario = jdbcVendas.checkEmail(email);
			
			conec.fecharConexao();
			return this.buildResponse(funcionario);

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
			List<JsonObject> listaVendas = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);
			listaVendas = jdbcVendas.buscar(cliente);
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaVendas);
			return this.buildResponse(json);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}				
	}
	@GET
	@Path("/buscarProdVendas")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarProdVendas(@QueryParam("valorBusca") String  cliente) {
		try {
			List<JsonObject> listaProdVendas = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);
			listaProdVendas = jdbcVendas.buscarProdVendas(cliente);
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaProdVendas);
			return this.buildResponse(json);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}				
	}
	@GET
	@Path("/buscarValorTotal")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarValorTotal(@QueryParam("valorBusca") String  cliente) {
		try {
			List<JsonObject> listaValorTotal = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);
			listaValorTotal = jdbcVendas.buscarValorTotal(cliente);
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaValorTotal);
			return this.buildResponse(json);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}				
	}
	@GET
	@Path("/checkIdProdVendas")
	@Produces(MediaType.APPLICATION_JSON)

	public Response checkId(@QueryParam("id")int id) {

		try {
			Vendas vendas= new Vendas();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);

			vendas = jdbcVendas.checkId(id);

			conec.fecharConexao();
			return this.buildResponse(vendas);

		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String vendasParam) {
		try {
			Vendas vendas = new Gson().fromJson(vendasParam, Vendas.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);

			boolean retorno = jdbcVendas.alterar(vendas);

			String msg="";
			if (retorno) {
				msg = "Quantidade alterada com sucesso!";
			}else {
				msg = "Erro ao alterar quantidade";
			}
			conec.fecharConexao();
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	@PUT
	@Path("/alterarIdFunc")
	@Consumes("application/*")
	public Response alterarIdFuncionario(String vendasParam) {
		try {
			Vendas vendas = new Gson().fromJson(vendasParam, Vendas.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);

			boolean retorno = jdbcVendas.alterarIdFuncio(vendas);

			String msg="";
			if (retorno) {
				msg = "idFuncionario alterado com sucesso!";
			}else {
				msg = "Erro ao alterar idFuncionario";
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
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);
			
			boolean retorno = jdbcVendas.deletar(id);
			
			String msg = "";
			if(retorno) {
				msg="Produto excluido da venda com sucesso!";
			}else {
				msg="Erro ao excluir produto da venda!";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}			
	}
	@DELETE
	@Path("/excluirVenda")
	@Consumes("application/*")
	public Response excluirVenda(@PathParam("id") int id) {
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendasDAO jdbcVendas = new JDBCVendasDAO(conexao);
			
			boolean retorno = jdbcVendas.deletarVendas();
			
			String msg = "";
			if(retorno) {
				msg="Venda excluida com sucesso!";
			}else {
				msg="Erro ao excluir a venda!";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}			
	}
}

