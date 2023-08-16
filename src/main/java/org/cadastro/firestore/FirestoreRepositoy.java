package org.cadastro.firestore;

import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.Filter;

import io.smallrye.mutiny.Uni;

public interface FirestoreRepositoy<T> {

	Uni<T> persist(T collection);

	Uni<Void> update(Map<String, Object> values, Filter filter);

	Uni<List<T>> findAll();

	Uni<T> find(String field, Object value);

	Uni<T> find(Filter filter);

	Uni<List<T>> findCollections(Filter filter);

	Uni<List<T>> findCollections(Filter filter, Integer limit);

	Uni<Boolean> exists(Filter lessThan);

	Uni<Void> deleteById(String id);

	Uni<Void> delete(Filter filter);

	FirestoreCollection fireStoreCollection();
}
