package com.todo.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.entity.RefreshToken;
import com.todo.entity.User;
import com.todo.repository.RefreshTokenRepository;
import com.todo.repository.UserRepository;
import com.todo.security.impl.UserDetailsImpl;
import com.todo.security.jwt.JwtConstants;
import com.todo.security.jwt.JwtGenerator;
import com.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UserService userService;
	private RefreshTokenRepository tokenRepository;

	public JwtAuthenticationFilter() {
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
												HttpServletResponse res) throws AuthenticationException {
		try {
			User user = new ObjectMapper()
					.readValue(req.getInputStream(), User.class);

			return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
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
		String accessToken = JwtGenerator.getAccessToken(auth);
		String refreshToken = JwtGenerator.getRefreshToken(auth);

		res.addHeader(JwtConstants.ACCESS_HEADER, JwtConstants.JWT_PREFIX + accessToken);
		res.addHeader(JwtConstants.REFRESH_HEADER, JwtConstants.JWT_PREFIX + refreshToken);

		String username = ((UserDetailsImpl) auth.getPrincipal()).getUsername();
		User user = userService.getUserIfPresent(username);

		tokenRepository.deleteAllByUserUsername(user.getUsername());
		tokenRepository.save(new RefreshToken(user, refreshToken));
	}

	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Autowired
	public void setTokenRepository(RefreshTokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}