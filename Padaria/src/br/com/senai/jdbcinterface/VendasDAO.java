package br.com.senai.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.senai.modelo.Produtos;
import br.com.senai.modelo.Vendas;
import br.com.senai.modelo.CadCategoria;
import br.com.senai.modelo.Funcionario;


public interface VendasDAO {
	public boolean inserir (Vendas vendas);
	public boolean inserirProdVendas (Vendas vendas);
	public List<JsonObject> buscar (String cliente);
	public List<JsonObject> buscarProdVendas (String cliente);
	public List<JsonObject> buscarValorTotal (String cliente);
	public Vendas checkId(int id);
	public boolean alterar(Vendas vendas);
	public boolean deletar(int id);
	public boolean deletarVendas();
	public Funcionario checkEmail(String email);
	public boolean alterarIdFuncio(Vendas vendas);
}