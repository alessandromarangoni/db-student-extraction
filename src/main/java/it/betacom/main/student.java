package it.betacom.main;

public class student {
	private String nome;
	private String Sede;

	public student(String nome, String sede){ 
		this.nome = nome;
		this.Sede = sede;
	}
	
	
	public String getSede() {
		return Sede;
	}
	public void setSede(String sede) {
		Sede = sede;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
