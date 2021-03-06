package com.todo.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static com.todo.security.jwt.JwtConstants.JWT_ADMIN;

public class JwtGenerator {

	public static String getAccessToken(Authentication authentication) {
		return Jwts.builder()
				   .setSubject(((UserDetails) authentication.getPrincipal()).getUsername())
				   .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.ACCESS_EXPIRATION))
				   .claim(JWT_ADMIN, authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
				   .signWith(SignatureAlgorithm.HS512, JwtConstants.ACCESS_SECRET.getBytes())
				   .compact();
	}

	public static String getRefreshToken(Authentication authentication) {
		return Jwts.builder()
				   .setSubject(((UserDetails) authentication.getPrincipal()).getUsername())
				   .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.REFRESH_EXPIRATION))
				   .claim(JWT_ADMIN, authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
				   .signWith(SignatureAlgorithm.HS512, JwtConstants.REFRESH_SECRET.getBytes())
				   .compact();
	}
}
