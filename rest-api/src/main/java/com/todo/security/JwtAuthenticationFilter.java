package com.todo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.entity.User;
import com.todo.security.impl.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
												HttpServletResponse res) throws AuthenticationException {
		try {
			User user = new ObjectMapper()
					.readValue(req.getInputStream(), User.class);

			UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					userDetails,
					userDetails.getPassword(),
					userDetails.getAuthorities()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {

		String token = Jwts.builder()
						   .setSubject(((UserDetailsImpl) auth.getPrincipal()).getUsername())
						   .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.JCT_EXPIRATION))
						   .signWith(SignatureAlgorithm.HS512, JwtConstants.JWT_SECRET.getBytes())
						   .compact();

		res.addHeader(JwtConstants.JWT_HEADER, JwtConstants.JWT_PREFIX + token);
	}
}