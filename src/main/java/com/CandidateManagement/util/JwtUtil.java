package com.CandidateManagement.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours

	private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public String generateToken(String email, String password, String firstName, String lastName,String role,String comapanyCode) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("companyCode", comapanyCode);
		claims.put("firstName", firstName);
		claims.put("lastName", lastName);
		claims.put("type", "ACCESS");
		claims.put("roles", role);
		claims.put("password", password);
		return createToken(claims, email);
	}

	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder().setClaims(claims).setSubject(email) 
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}
}
