package com.flab.rallymate.common.util.jwt.constant;

public enum TokenType {
	X_ACCESS_TOKEN("X-ACCESS-TOKEN"),
	X_REFRESH_TOKEN("X-REFRESH-TOKEN");

	public final String label;

	TokenType(String label) {
		this.label = label;
	}
}
