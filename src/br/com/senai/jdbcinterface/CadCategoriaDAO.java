package br.com.senai.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.senai.modelo.CadCategoria;




public interface CadCategoriaDAO {
	public boolean inserir (CadCategoria cadCategoria);
	public List<JsonObject> buscarPorNome(String nome);
	public CadCategoria checkId(int id);
	public boolean alterar(CadCategoria cadCategoria);
	public boolean deletar(int id);
	public List<CadCategoria> buscarSelCat();
}
