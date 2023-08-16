package org.cadastro.firestore.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.cloud.firestore.Query.Direction;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface OrderBy {

	Direction direction();
}
