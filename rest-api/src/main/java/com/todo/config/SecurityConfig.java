package com.todo.config;

import java.util.Arrays;

import com.todo.security.jwt.filter.JwtAuthenticationFilter;
import com.todo.security.jwt.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers(HttpMethod.POST, "/users", "/token/**");
		web.ignoring().antMatchers("/h2-console/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.and()
			.csrf().disable()
			.cors();
//			.authorizeRequests()
//			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//			.antMatchers("/users/{username}/**").access("principal == #username || hasRole('ROLE_ADMIN')")
//			.antMatchers("/token/**").authenticated() //todo return bad credentials
//			.anyRequest().authenticated()
//			.and()
//			.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//			.and()
//			.addFilter(jwtAuthenticationFilter())
//			.addFilter(jwtAuthorizationFilter())
			// this disables session creation on Spring Security
//			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter();
		authenticationFilter.setFilterProcessesUrl("/token/login");
		return authenticationFilter;
	}

	public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
		return new JwtAuthorizationFilter(authenticationManager());
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081"));
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("access-token", "refresh-token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
