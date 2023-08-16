package org.cadastro.firestore.annotation;

import com.google.cloud.firestore.Query.Direction;

public class OrderByValue {

	private String field;

	private Direction direction;

	public OrderByValue(String field, Direction direction) {

		this.field = field;
		this.direction = direction;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
