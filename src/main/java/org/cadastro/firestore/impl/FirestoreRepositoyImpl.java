package org.cadastro.firestore.impl;

import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.Filter;
import com.google.common.reflect.TypeToken;

import org.cadastro.firestore.FirestoreCollection;
import org.cadastro.firestore.FirestoreRepositoy;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

/**
 * Classe ancestral padrão para implementação do repository.<br>
 * Esta classe possui a implementação de todos os métodos padrões para
 * manipulação da collection.<br>
 * 
 * Author:Rafael Centenaro
 * 
 * @param <T>
 */
@Dependent
public class FirestoreRepositoyImpl<T> implements FirestoreRepositoy<T> {

	/**
	 * Permite fazer a leitura da classe parametrizada.
	 */
	private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {

		private static final long serialVersionUID = 1L;
	};

	@Inject
	FirestoreCollection fireStoreCollection;

	Class<T> collectionClass;

	@SuppressWarnings("unchecked")
	public FirestoreRepositoyImpl() {

		/**
		 * Busca a classe parametrizada. Esta classe será a collection.
		 */
		collectionClass = (Class<T>) typeToken.getType();

	}

	@Override
	public Uni<T> persist(T collection) {

		return fireStoreCollection.persist(collection);
	}

	@Override
	public Uni<Void> update(Map<String, Object> values, Filter filter) {
		return fireStoreCollection.update(collectionClass, values, filter);
	}

	@Override
	public Uni<List<T>> findAll() {
		return fireStoreCollection.findAll(collectionClass);
	}

	@Override
	public Uni<T> find(String field, Object value) {
		return fireStoreCollection.find(collectionClass, field, value);
	}

	@Override
	public Uni<T> find(Filter filter) {
		return fireStoreCollection.find(collectionClass, filter);
	}

	@Override
	public Uni<List<T>> findCollections(Filter filter) {
		return fireStoreCollection.findCollections(collectionClass, filter);
	}

	@Override
	public Uni<List<T>> findCollections(Filter filter, Integer limit) {
		return fireStoreCollection.findCollections(collectionClass, filter, limit);
	}

	@Override
	public Uni<Boolean> exists(Filter lessThan) {
		return fireStoreCollection.exists(collectionClass, lessThan);
	}

	@Override
	public Uni<Void> deleteById(String id) {
		return fireStoreCollection.deleteById(collectionClass, id);
	}

	@Override
	public Uni<Void> delete(Filter filter) {
		return fireStoreCollection.delete(collectionClass, filter);
	}

	/**
	 * Retorna a instância da classe responsável pela conexão ao firestore.<br>
	 * Com está instância é possível manipular qualquer collection sem a necessidade
	 * de um respositoy implementado.
	 */
	@Override
	public FirestoreCollection fireStoreCollection() {

		return this.fireStoreCollection;
	}

}
