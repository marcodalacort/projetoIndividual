package br.com.senai.modelo;

import java.io.Serializable;
import java.util.Date;

public class Vendas implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int idVendas;
	private int idProdutos;
	private int quantidadeVenda;
	private float valorVenda;
	private float valorTotalVenda;
	private String data;
	private int idFuncionario;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdVendas() {
		return idVendas;
	}
	public void setIdVendas(int idVendas) {
		this.idVendas = idVendas;
	}
	public int getIdProdutos() {
		return idProdutos;
	}
	public void setIdProdutos(int idProdutos) {
		this.idProdutos = idProdutos;
	}
	public int getQuantidadeVenda() {
		return quantidadeVenda;
	}
	public void setQuantidadeVenda(int quantidadeVenda) {
		this.quantidadeVenda = quantidadeVenda;
	}
	public float getValorVenda() {
		return valorVenda;
	}
	public void setValorVenda(float valorVenda) {
		this.valorVenda = valorVenda;
	}
	public float getValorTotalVenda() {
		return valorTotalVenda;
	}
	public void setValorTotalVenda(float valorTotalVenda) {
		this.valorTotalVenda = valorTotalVenda;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(int idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
}