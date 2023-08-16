package org.cadastro.pessoa.service.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.google.cloud.Timestamp;

public class DateUtil {

	private DateUtil() {

	}

	public static Timestamp parseDateTime(String date) {

		LocalDateTime data = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		Instant instant = data.atZone(ZoneId.systemDefault()).toInstant();

		return Timestamp.ofTimeSecondsAndNanos(instant.getEpochSecond(), instant.getNano());
	}

	public static Timestamp parseDate(String date) {

		LocalDate data = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		Instant instant = data.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

		return Timestamp.ofTimeSecondsAndNanos(instant.getEpochSecond(), instant.getNano());
	}

}
