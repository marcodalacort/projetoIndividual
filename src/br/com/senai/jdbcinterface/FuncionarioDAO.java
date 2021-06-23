package br.com.senai.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.senai.modelo.Funcionario;

public interface FuncionarioDAO {
	public List<JsonObject> buscarPorMatricula(String matricula);
	public Funcionario checkId(int id);
	public boolean alterar(Funcionario funcionario);
	public boolean inserir (Funcionario funcionario);
	public boolean deletar(int id);
	public Funcionario checkIdSenha(int id);
	public boolean alterarSenha(Funcionario funcionario);
	
}
