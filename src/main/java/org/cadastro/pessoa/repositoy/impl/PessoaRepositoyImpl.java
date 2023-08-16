package org.cadastro.pessoa.repositoy.impl;

import org.cadastro.firestore.collection.PessoaCollection;
import org.cadastro.firestore.impl.FirestoreRepositoyImpl;
import org.cadastro.pessoa.repositoy.PessoaRepositoy;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Para usar o padrão repository deve-se extender da classe
 * FirestoreRepositoyImpl e informar qual collection este respository será
 * responsável. Desta forma o repository herdará todos os métodos padrões para
 * manipulação da collection.
 */
@ApplicationScoped
public class PessoaRepositoyImpl extends FirestoreRepositoyImpl<PessoaCollection> implements PessoaRepositoy {

}
