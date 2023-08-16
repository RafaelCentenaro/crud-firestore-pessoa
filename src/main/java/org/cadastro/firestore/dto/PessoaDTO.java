package org.cadastro.firestore.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PessoaDTO {

	private String id;

	private String nome;

	private String dataNascimento;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

}
