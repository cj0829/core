package org.csr.core.page;

import java.util.Locale;

public enum Direction {
	ASC, DESC;
	public static Direction fromString(String value) {
		try {
			return Direction.valueOf(value.toUpperCase(Locale.US));
		} catch (Exception e) {
			throw new IllegalArgumentException(
					String.format(
							"Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).",
							value), e);
		}
	}
}