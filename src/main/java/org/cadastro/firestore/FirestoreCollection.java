package org.cadastro.firestore;

import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.Filter;

import io.smallrye.mutiny.Uni;

public interface FirestoreCollection {

	<T> Uni<T> persist(T collection);

	<T> Uni<Void> update(Class<T> collection, Map<String, Object> values, Filter filter);

	<T> Uni<List<T>> findAll(Class<T> collection);

	<T> Uni<T> find(Class<T> collection, String field, Object value);

	<T> Uni<T> find(Class<T> collection, Filter filter);

	<T> Uni<List<T>> findCollections(Class<T> collection, Filter filter);

	<T> Uni<List<T>> findCollections(Class<T> collection, Filter filter, Integer limit);

	<T> Uni<Boolean> exists(Class<T> collection, Filter lessThan);

	<T> Uni<Void> deleteById(Class<T> collection, String id);

	<T> Uni<Void> delete(Class<T> collection, Filter filter);

}
