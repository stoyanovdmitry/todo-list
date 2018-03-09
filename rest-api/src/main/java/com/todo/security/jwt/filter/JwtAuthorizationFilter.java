package com.todo.security.jwt.filter;

import com.todo.security.jwt.JwtConstants;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private UserDetailsService userDetailsService;

	public JwtAuthorizationFilter(AuthenticationManager authManager, UserDetailsService userDetailsService) {
		super(authManager);
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
									HttpServletResponse res,
									FilterChain chain) throws IOException, ServletException {
		String header = req.getHeader(JwtConstants.JWT_HEADER);

		if (header == null || !header.startsWith(JwtConstants.JWT_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(JwtConstants.JWT_HEADER);
		if (token != null) {
			String username = Jwts.parser()
								  .setSigningKey(JwtConstants.JWT_SECRET.getBytes())
								  .parseClaimsJws(token.replace(JwtConstants.JWT_PREFIX, ""))
								  .getBody()
								  .getSubject();

			if (username != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				return new UsernamePasswordAuthenticationToken(userDetails,
															   userDetails.getPassword(),
															   userDetails.getAuthorities());
			}
			return null;
		}
		return null;
	}
}