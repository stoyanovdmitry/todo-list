package com.todo.security.jwt;

public class JwtConstants {

	public static final String JWT_SECRET = "secret";
	public static final long JCT_EXPIRATION = 864_000_000; // 10 days
	public static final String JWT_PREFIX = "Bearer ";
	public static final String JWT_HEADER = "Authorization";
	public static final String JWT_ADMIN = "admin";
}
