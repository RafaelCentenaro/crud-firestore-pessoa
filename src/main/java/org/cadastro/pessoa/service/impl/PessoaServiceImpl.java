package org.cadastro.pessoa.service.impl;

import java.util.List;

import org.cadastro.firestore.collection.PessoaCollection;
import org.cadastro.firestore.dto.PessoaDTO;
import org.cadastro.pessoa.repositoy.PessoaRepositoy;
import org.cadastro.pessoa.service.PessoaService;
import org.cadastro.pessoa.service.util.DateUtil;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PessoaServiceImpl implements PessoaService {

	@Inject
	PessoaRepositoy pessoaRepositoy;

	/**
	 * Recebe o objeto PessoaDTO e faz a conversão para PessoaCollection. Na
	 * sequência envia PessoaCollection para persistência.<br>
	 * O método persist() irá tratar se este objeto é inclusão ou edição.
	 */
	@Override
	public Uni<Void> salvar(PessoaDTO pessoaDTO) {

		PessoaCollection collection = new PessoaCollection();
		collection.setId(pessoaDTO.getId());
		collection.setNome(pessoaDTO.getNome());
		collection.setDataNascimento(DateUtil.parseDate(pessoaDTO.getDataNascimento()));

		return pessoaRepositoy.persist(collection).replaceWithVoid();
	}

	/**
	 * Retorna uma lista com todos os objetos da collection.
	 */
	@Override
	public Uni<List<PessoaCollection>> listar() {
		return pessoaRepositoy.findAll();
	}

	/**
	 * Busca um determinado registro da collection.
	 */
	@Override
	public Uni<PessoaCollection> adquirir(String codigoPessoa) {

		return pessoaRepositoy.find("id", codigoPessoa);
	}

	/**
	 * Remove um determinado registro da collection.
	 */
	@Override
	public Uni<Void> excluir(String codigoPessoa) {
		return pessoaRepositoy.deleteById(codigoPessoa);
	}

}
