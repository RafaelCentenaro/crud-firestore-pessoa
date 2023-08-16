package org.cadastro.firestore.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.AggregateQuery;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import org.cadastro.firestore.FirestoreCollection;
import org.cadastro.firestore.annotation.Collection;
import org.cadastro.firestore.annotation.OrderBy;
import org.cadastro.firestore.annotation.OrderByValue;
import org.cadastro.pessoa.service.util.IfNull;
import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;

/**
 * Classe responsável pela conexão ao Firestore e manipulação das
 * collections.<br>
 * 
 * Author:Rafael Centenaro
 *
 */
@Singleton
public class FirestoreCollectionImpl implements FirestoreCollection {

	/**
	 * Representa um banco de dados do Firestore e é o ponto de entrada para todas
	 * as operações do Firestore. Obs: Fornecido pela biblioteca.
	 */
	private final Firestore db;

	String projectId;

	String credentialsFile;

	String collectionRoot;

	String collectionLeaf;

	/**
	 * O Contrutor será responsável pela incialização do Firestore.
	 */
	public FirestoreCollectionImpl() {

		try {

			loadProperties();

			ClassLoader classLoader = FirestoreCollectionImpl.class.getClassLoader();

			Log.info("Initializing Firestore connection");

			GoogleCredentials credentials = GoogleCredentials
					.fromStream(classLoader.getResourceAsStream(credentialsFile));

			FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().setCredentials(credentials)
					.setProjectId(projectId).build();

			this.db = firestoreOptions.getService();

			Log.info("Firestore connection initialized");

		} catch (IOException e) {

			Log.error("Failed to initialize Firestore connection", e);

			throw new RuntimeException(e);
		}
	}

	/**
	 * Busca as propriedades do arquivo application.properties
	 */
	private void loadProperties() {

		SmallRyeConfig config = ConfigProvider.getConfig().unwrap(SmallRyeConfig.class);

		this.projectId = config.getRawValue("firestore.projectId");
		this.credentialsFile = config.getRawValue("firestore.credentialsFile");
		this.collectionRoot = config.getRawValue("firestore.collectionRoot");
		this.collectionLeaf = IfNull.get(config.getRawValue("firestore.collectionLeaf"), "documents");

		if (this.collectionRoot == null || this.projectId == null || this.credentialsFile == null) {
			throw new RuntimeException(
					"Configuração não definida. As propriedades firestore.projectId,firestore.collectionRoot e firestore.projectId são obrigatórias.");
		}
	}

	/**
	 * Valida o objeto da collection e retorna o nome.
	 * 
	 * @param <T>
	 * @param collection
	 * @return
	 */
	private <T> String validarCollection(T collection) {

		Class<?> type = null;
		if (collection.getClass().equals(Class.class)) {
			type = (Class<?>) collection;
		} else {
			type = collection.getClass();
		}

		RegisterForReflection registerForReflection = type.getAnnotation(RegisterForReflection.class);

		if (registerForReflection == null) {
			throw new WebApplicationException("A classe " + type.getName()
					+ " precisa ser anotada com @RegisterForReflection para permitir build nativa.");
		}

		Collection collectionAnnotation = type.getAnnotation(Collection.class);

		if (collectionAnnotation == null) {
			throw new WebApplicationException("A classe " + type.getName()
					+ " precisa ser anotada com @Collection para identificação da collection no FireStore.");
		}

		return collectionAnnotation.name();
	}

	/**
	 * Grava a collection e retorna o objeto preenchido com o id.<br>
	 * Este método insere quando o id for nulo, ou edita quando já existir um id.
	 */
	@Override
	public <T> Uni<T> persist(T collection) {

		CollectionReference collectionReference = getCollectionReference(collection);

		Object idValue = getId(collection);

		DocumentReference docRef = idValue != null && !idValue.equals("string")
				? collectionReference.document(idValue.toString())
				: collectionReference.document();

		String id = docRef.getId();

		setId(collection, id);

		ApiFuture<WriteResult> result = docRef.set(collection);

		try {
			result.get();

			return Uni.createFrom().item(collection);

		} catch (Exception e) {

			return Uni.createFrom().failure(e);
		}

	}

	/**
	 * Responsável por fazer update na collection informada.
	 */
	@Override
	public <T> Uni<Void> update(Class<T> collection, Map<String, Object> values, Filter filter) {

		CollectionReference collectionReference = getCollectionReference(collection);

		Query query = collectionReference.where(filter);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		try {
			List<QueryDocumentSnapshot> list = querySnapshot.get().getDocuments();

			for (QueryDocumentSnapshot doc : list) {
				doc.getReference().update(values).get();
			}

		} catch (Exception e) {
			return Uni.createFrom().failure(e);
		}

		return Uni.createFrom().voidItem();

	}

	/**
	 * Busca todos os registros de uma collection
	 */
	@Override
	public <T> Uni<List<T>> findAll(Class<T> collection) {

		CollectionReference collectionReference = getCollectionReference(collection);

		OrderByValue orderBy = this.getOrderBy(collection);

		ApiFuture<QuerySnapshot> future = null;

		if (orderBy != null) {
			future = collectionReference.orderBy(orderBy.getField(), orderBy.getDirection()).get();
		} else {
			future = collectionReference.get();
		}

		ApiFuture<QuerySnapshot> futureFinal = future;

		Uni<List<T>> result = null;

		try {
			result = Uni.createFrom().item(futureFinal.get().toObjects(collection));
		} catch (Exception e) {
			return Uni.createFrom().failure(e);
		}

		return result;

	}

	/**
	 * Busca os dados de uma collection através do filtro único.
	 */
	@Override
	public <T> Uni<T> find(Class<T> collection, String field, Object value) {

		return this.find(collection, Filter.equalTo(field, value));
	}

	/**
	 * Busca os dados de uma collection através do filtro composto.
	 */
	@Override
	public <T> Uni<T> find(Class<T> collection, Filter filter) {

		CollectionReference collectionReference = getCollectionReference(collection);

		Query query = collectionReference.where(filter);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		try {
			List<QueryDocumentSnapshot> list = querySnapshot.get().getDocuments();

			return Uni.createFrom().item(() -> !list.isEmpty() ? list.get(0).toObject(collection) : null);

		} catch (Exception e) {
			return Uni.createFrom().failure(e);
		}

	}

	/**
	 * Busca uma lista da collection informada com o filtro informado.
	 */
	@Override
	public <T> Uni<List<T>> findCollections(Class<T> collection, Filter filter) {

		return findCollections(collection, filter, 0);
	}

	/**
	 * Busca uma lista da collection informada com o filtro informado e com limite;
	 */
	@Override
	public <T> Uni<List<T>> findCollections(Class<T> collection, Filter filter, Integer limit) {

		CollectionReference collectionReference = getCollectionReference(collection);

		OrderByValue orderBy = this.getOrderBy(collection);

		ApiFuture<QuerySnapshot> future = null;

		if (orderBy != null) {

			Query query = filter != null
					? collectionReference.where(filter).orderBy(orderBy.getField(), orderBy.getDirection())
					: collectionReference.orderBy(orderBy.getField(), orderBy.getDirection());

			if (limit > 0) {
				query = query.limit(limit);
			}

			future = query.get();

		} else {

			Query query = filter != null ? collectionReference.where(filter) : collectionReference;

			if (limit > 0) {
				query = query.limit(limit);
			}

			future = query.get();
		}

		ApiFuture<QuerySnapshot> futureFinal = future;

		List<T> result = null;
		try {
			result = futureFinal.get().toObjects(collection);
		} catch (Exception e) {
			return Uni.createFrom().failure(e);
		}

		return Uni.createFrom().item(result);

	}

	/**
	 * Consulta se existe dados para uma determinada collection buscando pelo filtro
	 * informado.
	 */
	@Override
	public <T> Uni<Boolean> exists(Class<T> collection, Filter filter) {

		CollectionReference collectionReference = getCollectionReference(collection);

		Query query = filter != null ? collectionReference.select("id").where(filter)
				: collectionReference.select("id");

		AggregateQuery queryCount = query.limit(1).count();

		try {
			return Uni.createFrom().item(queryCount.get().get().getCount() > 0);
		} catch (Exception e) {
			return Uni.createFrom().failure(e);
		}

	}

	/**
	 * Deleta a collection por id.
	 */
	@Override
	public <T> Uni<Void> deleteById(Class<T> collection, String id) {

		CollectionReference collectionReference = getCollectionReference(collection);

		ApiFuture<WriteResult> result = collectionReference.document(id).delete();

		try {
			result.get();

		} catch (Exception e) {
			return Uni.createFrom().failure(e);
		}

		return Uni.createFrom().voidItem();

	}

	/**
	 * Deleta a collection pelo filtro informado.
	 */
	@Override
	public <T> Uni<Void> delete(Class<T> collection, Filter filter) {

		CollectionReference collectionReference = getCollectionReference(collection);

		Query query = collectionReference.select("id").where(filter);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		try {
			List<QueryDocumentSnapshot> list = querySnapshot.get().getDocuments();

			for (QueryDocumentSnapshot document : list) {
				collectionReference.document(document.getId()).delete().get();
			}

		} catch (Exception e) {
			return Uni.createFrom().failure(e);
		}

		return Uni.createFrom().voidItem();

	}

	private <T> CollectionReference getCollectionReference(T collection) {

		String collectionName = validarCollection(collection);

		return db.collection(collectionRoot + "/" + collectionName + "/" + this.collectionLeaf);

	}

	/**
	 * Busca a ordenação padrão informada na classe da collection.
	 * 
	 * @param <T>
	 * @param collection
	 * @return
	 */

	private <T> OrderByValue getOrderBy(T collection) {

		Class<T> type = getType(collection);

		Field[] fields = type.getDeclaredFields();

		for (Field field : fields) {

			if (field.getAnnotation(OrderBy.class) != null) {

				return new OrderByValue(field.getName(), field.getAnnotation(OrderBy.class).direction());

			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> getType(T collection) {

		Class<T> type = null;
		if (collection.getClass().equals(Class.class)) {
			type = (Class<T>) collection;
		} else {
			type = (Class<T>) collection.getClass();
		}
		return type;
	}

	/**
	 * Atribui o id gerado a collection informada.
	 * 
	 * @param <T>
	 * @param collection
	 * @param id
	 */
	private <T> void setId(T collection, String id) {

		try {

			Class<T> type = getType(collection);

			Field[] fields = type.getDeclaredFields();

			for (Field field : fields) {

				if (field.getName().equals("id")) {

					field.setAccessible(true);

					field.set(collection, id);

					return;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Busca o id da collection informada.
	 * 
	 * @param <T>
	 * @param collection
	 * @return
	 */
	private <T> Object getId(T collection) {

		Object value = null;
		try {

			Class<T> type = getType(collection);

			Field[] fields = type.getDeclaredFields();

			for (Field field : fields) {

				if (field.getName().equals("id")) {

					field.setAccessible(true);

					return field.get(collection);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
