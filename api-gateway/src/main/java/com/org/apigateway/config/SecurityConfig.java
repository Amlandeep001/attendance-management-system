package com.org.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;

@Configuration
public class SecurityConfig
{
	private final String issuerUri;

	public SecurityConfig(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri)
	{
		this.issuerUri = issuerUri;
	}

	@Bean
	ReactiveJwtDecoder reactiveJwtDecoder()
	{
		return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
	}
}
