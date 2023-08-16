package org.cadastro.pessoa.api.impl;

import java.util.List;

import org.cadastro.firestore.collection.PessoaCollection;
import org.cadastro.firestore.dto.PessoaDTO;
import org.cadastro.pessoa.api.PessoaController;
import org.cadastro.pessoa.service.PessoaService;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

public class PessoaControllerImpl implements PessoaController {

	@Inject
	PessoaService pessoaService;

	@Override
	public Uni<Void> salvar(PessoaDTO pessoaDTO) {

		return pessoaService.salvar(pessoaDTO);
	}

	@Override
	public Uni<List<PessoaCollection>> listar() {
		return pessoaService.listar();
	}

	@Override
	public Uni<PessoaCollection> adquirir(String codigoPessoa) {
		return pessoaService.adquirir(codigoPessoa);
	}

	@Override
	public Uni<Void> excluir(String codigoPessoa) {
		return pessoaService.excluir(codigoPessoa);
	}

}
