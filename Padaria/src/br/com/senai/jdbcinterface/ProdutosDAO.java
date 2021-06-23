package br.com.senai.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.senai.modelo.Produtos;
import br.com.senai.modelo.CadCategoria;


public interface ProdutosDAO {
	public boolean inserir (Produtos produtos);
	public List<JsonObject> buscar (String cliente);
	public Produtos checkId(int id);
	public boolean alterar(Produtos produtos);
	public boolean deletar(int id);
}