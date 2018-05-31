package com.todo.security.jwt;

public class JwtConstants {

	public static final String JWT_ADMIN = "admin";
	public static final String JWT_HEADER = "Authorization";
	public static final String JWT_PREFIX = "Bearer ";

	public static final String ACCESS_SECRET = "secret";
	public static final long ACCESS_EXPIRATION = 864_000_000; // 864_000_000 = 10 days
	public static final String ACCESS_HEADER = "Access-Token";

	public static final String REFRESH_SECRET = "refresh";
	public static final long REFRESH_EXPIRATION = 2_592_000_000L;	// 2_592_000_000L = 30 days
	public static final String REFRESH_HEADER = "Refresh-Token";
}
