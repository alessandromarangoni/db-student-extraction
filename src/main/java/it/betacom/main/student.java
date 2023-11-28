package it.betacom.main;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Student {
	private String nome;
	private String Sede;
	private LocalDateTime dataEstrazione;
	private int extractionCount;

	public Student(String nome, String sede, LocalDateTime dataEstrazione){ 
		this.nome = nome;
		this.Sede = sede;
		this.dataEstrazione = dataEstrazione;
		this.extractionCount += 1;
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


	public LocalDateTime getDataEstrazione() {
		return dataEstrazione;
	}

	public void setDataEstrazione(LocalDateTime dataEstrazione) {
		this.dataEstrazione = dataEstrazione;
	}
	

	public int getExtractionCount() {
		return extractionCount;
	}


	public void setExtractionCount(int extractionCount) {
		this.extractionCount = extractionCount;
	}
	
	 public void incrementExtractionCount() {
	        this.extractionCount++;
	    }

}
