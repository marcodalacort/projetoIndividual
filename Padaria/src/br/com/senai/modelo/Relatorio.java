package br.com.senai.modelo;

import java.io.Serializable;
import java.util.Date;

public class Relatorio implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int idVendas;
	private String nomeProdutos;
	private float valorUnitario;
	private int quantidadeVenda;
	private float valorParcial;
	private float valorTotalVenda;
	private String data;
	private String nomeFuncionario;
	
	
	
	public int getIdVendas() {
		return idVendas;
	}
	public void setIdVendas(int idVendas) {
		this.idVendas = idVendas;
	}
	public String getNomeProdutos() {
		return nomeProdutos;
	}
	public void setNomeProdutos(String nomeProdutos) {
		this.nomeProdutos = nomeProdutos;
	}
	public int getQuantidadeVenda() {
		return quantidadeVenda;
	}
	public void setQuantidadeVenda(int quantidadeVenda) {
		this.quantidadeVenda = quantidadeVenda;
	}
	public float getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public float getValorParcial() {
		return valorParcial;
	}
	public void setValorParcial(float valorParcial) {
		this.valorParcial = valorParcial;
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
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
}