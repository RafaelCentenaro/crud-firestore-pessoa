package org.cadastro.firestore.collection;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Query.Direction;

import org.cadastro.firestore.annotation.Collection;
import org.cadastro.firestore.annotation.OrderBy;
import org.cadastro.pessoa.service.util.LocalDateDeserializer;
import org.cadastro.pessoa.service.util.LocalDateSerializer;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@Collection(name = "PessoaCollection")
public class PessoaCollection {

	private String id;

	@OrderBy(direction = Direction.ASCENDING)
	private String nome;

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private Timestamp dataNascimento;

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

	public Timestamp getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Timestamp dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

}
