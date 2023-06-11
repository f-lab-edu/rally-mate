package com.flab.rallymate.domain.oauth.constant;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class OAuthTypeConverter implements Converter<String, OAuthType> {

	@Override
	public OAuthType convert(String s) {
		return OAuthType.valueOf(s.toUpperCase());
	}
}
