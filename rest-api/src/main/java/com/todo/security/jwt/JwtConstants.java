package com.todo.security.jwt;

public class JwtConstants {

	public static final String JWT_SECRET = "secret";
	public static final String JWT_ADMIN = "admin";
	public static final String JWT_HEADER = "Authorization";
	public static final String JWT_PREFIX = "Bearer ";

	public static final long ACCESS_EXPIRATION = 10_000; // 864_000_000 = 10 days
	public static final String ACCESS_HEADER = "Access-Token";

	public static final long REFRESH_EXPIRATION = 60_000;
	public static final String REFRESH_HEADER = "Refresh-Token";
}
