package com.todo.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.entity.RefreshToken;
import com.todo.entity.User;
import com.todo.repository.RefreshTokenRepository;
import com.todo.security.jwt.JwtConstants;
import com.todo.security.impl.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.todo.security.jwt.JwtConstants.JWT_ADMIN;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private RefreshTokenRepository tokenRepository;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
								   RefreshTokenRepository tokenRepository) {
		this.authenticationManager = authenticationManager;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
												HttpServletResponse res) throws AuthenticationException {
		try {
			User user = new ObjectMapper()
					.readValue(req.getInputStream(), User.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					user.getUsername(),
					user.getPassword()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {

		String username = ((UserDetailsImpl) auth.getPrincipal()).getUsername();

		String accessToken = Jwts.builder()
								 .setSubject(username)
								 .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.ACCESS_EXPIRATION))
								 .claim(JWT_ADMIN, auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
								 .signWith(SignatureAlgorithm.HS512, JwtConstants.JWT_SECRET.getBytes())
								 .compact();

		String refreshToken = Jwts.builder()
						   .setSubject(username)
						   .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.REFRESH_EXPIRATION))
						   .claim(JWT_ADMIN, auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
						   .signWith(SignatureAlgorithm.HS512, JwtConstants.JWT_SECRET.getBytes())
						   .compact();

		res.addHeader(JwtConstants.ACCESS_HEADER, JwtConstants.JWT_PREFIX + accessToken);
		res.addHeader(JwtConstants.REFRESH_HEADER, JwtConstants.JWT_PREFIX + refreshToken);

		tokenRepository.save(new RefreshToken(username, refreshToken));
	}
}