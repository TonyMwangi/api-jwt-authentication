package com.mwangianthony.backend.dev.services;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtServices {

	String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
	String generateToken(UserDetails userDetails);
	String extractUsername(String token);
	boolean isTokenValid(String token, UserDetails userDetails);
	boolean isTokenExpired(String token);
	Date extractTokenExpiration(String token);
	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);
	Claims extractClaims(String token);
	Key getSigningKey();
}
