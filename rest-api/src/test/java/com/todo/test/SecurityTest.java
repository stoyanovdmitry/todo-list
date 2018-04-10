package com.todo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.entity.User;
import com.todo.security.jwt.JwtConstants;
import com.todo.security.jwt.JwtGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void getUser_withoutToken_thenIsUnauthorized() throws Exception {
		mvc.perform(get("/users/user"))
		   .andExpect(status().isUnauthorized());
	}

	@Test
	public void getUser_withToken_thenIsOk() throws Exception {

		String token = getAccessToken("user");

		mvc.perform(get("/users/user")
							.header("Authorization", JwtConstants.JWT_PREFIX + token))
		   .andExpect(status().isOk());
	}

	@Test
	public void getUser_withToken_thenIsForbidden() throws Exception {

		String token = getAccessToken("user");

		mvc.perform(get("/users/admin")
							.header("Authorization", JwtConstants.JWT_PREFIX + token))
		   .andExpect(status().isForbidden());
	}

	@Test
	public void getToken_correctCredentials_thenIsOk() throws Exception {

		User user = new User("user", "password");
		String jsonUser = objectMapper.writeValueAsString(user);

		mvc.perform(get("/token/login").accept(MediaType.APPLICATION_JSON_UTF8)
									   .content(jsonUser))
		   .andExpect(status().isOk());
	}

	private String getAccessToken(String username) {
		UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getUsername()).thenReturn(username);

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(authentication.getAuthorities()).thenReturn(Collections.emptyList());

		return JwtGenerator.getAccessToken(authentication);
	}
}
