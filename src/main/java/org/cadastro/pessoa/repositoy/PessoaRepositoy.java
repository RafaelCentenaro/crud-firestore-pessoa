package org.cadastro.pessoa.repositoy;

import org.cadastro.firestore.FirestoreRepositoy;
import org.cadastro.firestore.collection.PessoaCollection;

/**
 * Para usar o padrão repository deve-se extender da interface
 * FirestoreRepositoy e informar qual collection este respository será
 * responsável.
 */
public interface PessoaRepositoy extends FirestoreRepositoy<PessoaCollection> {

}
