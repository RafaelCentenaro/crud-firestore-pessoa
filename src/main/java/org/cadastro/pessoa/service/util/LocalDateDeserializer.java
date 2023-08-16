package org.cadastro.pessoa.service.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.cloud.Timestamp;

public class LocalDateDeserializer extends JsonDeserializer<Timestamp> {

	@Override
	public Timestamp deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {

		return DateUtil.parseDate(jsonParser.getText());

	}
}