package com.org.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.org.apigateway.factory.OAuth2TokenValidatorGatewayFilterFactory;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class GateWaySecurityConfig
{
	private final OAuth2TokenValidatorGatewayFilterFactory oAuth2TokenValidatorGatewayFilterFactory;
	private final ReactiveJwtDecoder reactiveJwtDecoder;

	public GateWaySecurityConfig(OAuth2TokenValidatorGatewayFilterFactory oAuth2TokenValidatorGatewayFilterFactory, ReactiveJwtDecoder reactiveJwtDecoder)
	{
		this.oAuth2TokenValidatorGatewayFilterFactory = oAuth2TokenValidatorGatewayFilterFactory;
		this.reactiveJwtDecoder = reactiveJwtDecoder;
	}

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, WebFilter gatewayFilterAdapter)
	{
		http
				.csrf(csrf -> csrf.disable())
				.authorizeExchange(authorize -> authorize
						.pathMatchers("/actuator/**").permitAll() // Allow actuator end-point
						.pathMatchers(HttpMethod.OPTIONS).permitAll() // Allow pre-flight requests
						.pathMatchers("/oauth2/token").permitAll() // Allow token end-point
						.anyExchange().authenticated())
				.oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt
						.jwtDecoder(reactiveJwtDecoder)));
		// .oauth2ResourceServer(oauth2 -> oauth2
		// .jwt(Customizer.withDefaults()));

		http
				.addFilterBefore(gatewayFilterAdapter, SecurityWebFiltersOrder.FIRST)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance()); // Making session stateless

		return http.build();
	}

	@Bean
	WebFilter gatewayFilterAdapter()
	{
		return new WebFilter()
		{
			private final GatewayFilter gatewayFilter = oAuth2TokenValidatorGatewayFilterFactory.apply(new Object());

			@Override
			public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain)
			{
				return gatewayFilter.filter(exchange, new GatewayFilterChain()
				{
					@Override
					public Mono<Void> filter(ServerWebExchange exchange)
					{
						return chain.filter(exchange);
					}
				});
			}
		};
	}
}
