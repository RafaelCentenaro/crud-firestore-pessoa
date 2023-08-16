package org.cadastro.pessoa.service;

import java.util.List;

import org.cadastro.firestore.collection.PessoaCollection;
import org.cadastro.firestore.dto.PessoaDTO;

import io.smallrye.mutiny.Uni;

public interface PessoaService {

	Uni<Void> salvar(PessoaDTO pessoaDTO);

	Uni<List<PessoaCollection>> listar();

	Uni<PessoaCollection> adquirir(String codigoPessoa);

	Uni<Void> excluir(String codigoPessoa);

}
