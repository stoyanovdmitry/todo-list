package com.todo.config;

import com.todo.security.jwt.filter.JwtAuthenticationFilter;
import com.todo.security.jwt.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/users").permitAll()
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/users/{username}/**").access("principal.username == #username || hasRole('ROLE_ADMIN')")
			.anyRequest().authenticated()
			.and()
			.addFilter(new JwtAuthenticationFilter(authenticationManager(), userDetailsService))
			.addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService))
			// this disables session creation on Spring Security
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
