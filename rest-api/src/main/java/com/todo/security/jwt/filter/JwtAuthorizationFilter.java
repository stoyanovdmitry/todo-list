package com.todo.security.jwt.filter;

import com.todo.security.jwt.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.todo.security.jwt.JwtConstants.JWT_ADMIN;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	public JwtAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
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
			Claims body = Jwts.parser()
							  .setSigningKey(JwtConstants.ACCESS_SECRET.getBytes())
							  .parseClaimsJws(token.replace(JwtConstants.JWT_PREFIX, ""))
							  .getBody();

			String username = body.getSubject();

			if (username != null) {
				boolean admin = (boolean) body.get(JWT_ADMIN);
				List<GrantedAuthority> authorityList = new ArrayList<>();

				if (admin) {
					authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
				}

				return new UsernamePasswordAuthenticationToken(username,
															   null,
															   authorityList);
			}
		}
		return null;
	}
}