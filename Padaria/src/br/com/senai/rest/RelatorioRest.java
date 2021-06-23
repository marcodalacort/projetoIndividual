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
import br.com.senai.jdbc.JDBCRelatorioDAO;
import br.com.senai.modelo.CadCategoria;
import br.com.senai.modelo.Cargos;
import br.com.senai.modelo.Funcionario;
import br.com.senai.modelo.Vendas;
import br.com.senai.modelo.Relatorio;




@Path("relatorio")
public class RelatorioRest extends UtilRest{
	
	@GET
	@Path("/buscar")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@QueryParam("valorBusca") String  campoBusca) {
		try {
			List<JsonObject> listaVendas = new ArrayList<JsonObject>();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCRelatorioDAO jdbcRelatorio = new JDBCRelatorioDAO(conexao);
			listaVendas = jdbcRelatorio.buscar(campoBusca);
		
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaVendas);
			return this.buildResponse(json);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}				
	}

	@GET
	@Path("/buscarData")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarData(@QueryParam("valorDataIni") String  dataIni,
						   @QueryParam("valorDataFin") String  dataFin) {
		try {
			List<JsonObject> listaVendas = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCRelatorioDAO jdbcRelatorio = new JDBCRelatorioDAO(conexao);
			listaVendas = jdbcRelatorio.buscarData(dataIni, dataFin);
		
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaVendas);
			return this.buildResponse(json);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}				
	}
	
	
}