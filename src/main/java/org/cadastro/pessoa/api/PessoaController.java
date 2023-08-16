package org.cadastro.pessoa.api;

import java.util.List;

import org.cadastro.firestore.collection.PessoaCollection;
import org.cadastro.firestore.dto.PessoaDTO;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/pessoa")
@Tag(name = "PessoaController", description = "Controller responsável pela manutenção do cadastro de pessoas.")
public interface PessoaController {

	@POST
	@Path("/salvar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Serviço responsável pela inclusão e edição.")
	public abstract Uni<Void> salvar(PessoaDTO pessoaDTO);

	@GET
	@Path("/listar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Serviço responsável pela listagem.")
	public abstract Uni<List<PessoaCollection>> listar();

	@GET
	@Path("/adquirir/{codigoPessoa}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Serviço responsável por buscar um determinado registro.")
	public abstract Uni<PessoaCollection> adquirir(@PathParam("codigoPessoa") String codigoPessoa);

	@DELETE
	@Path("/excluir/{codigoPessoa}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Serviço responsável pela exclusão de um determinado registro.")
	public abstract Uni<Void> excluir(@PathParam("codigoPessoa") String codigoPessoa);
}
