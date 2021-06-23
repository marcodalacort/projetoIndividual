package br.com.senai.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.senai.modelo.Funcionario;

public interface RelatorioDAO {
	public List<JsonObject> buscarData (String dataIni, String dataFin);
	public List<JsonObject> buscar(String campoBusca);
	
}
