package com.org.apigateway.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class OAuth2TokenValidatorGatewayFilterFactory extends AbstractGatewayFilterFactory<Object>
{
	private final ReactiveJwtDecoder reactiveJwtDecoder;

	public OAuth2TokenValidatorGatewayFilterFactory(ReactiveJwtDecoder reactiveJwtDecoder)
	{
		this.reactiveJwtDecoder = reactiveJwtDecoder;
	}

	@Override
	public GatewayFilter apply(Object config)
	{
		return (exchange, chain) ->
		{
			if(config == null)
			{
				// Handle null configuration gracefully
				// For example, you can log a warning message or simply return the chain
				return chain.filter(exchange);
			}

			// Proceed with the normal filter logic
			String token = exchange.getRequest().getHeaders().getFirst("Authorization");
			if(token == null || !token.startsWith("Bearer "))
			{
				return onError(exchange, "Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
			}

			String jwtToken = token.substring(7);

			return reactiveJwtDecoder.decode(jwtToken)
					.flatMap(jwt -> chain.filter(exchange))
					.onErrorResume(AuthenticationException.class, e -> onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED));
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus)
	{
		exchange.getResponse().setStatusCode(httpStatus);
		return exchange.getResponse().setComplete();
	}
}
