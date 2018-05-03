package com.todo.test;

import com.todo.entity.RefreshToken;
import com.todo.entity.Todo;
import com.todo.entity.User;
import com.todo.repository.RefreshTokenRepository;
import com.todo.repository.TodoRepository;
import com.todo.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoriesCustomMethodsTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private RefreshTokenRepository tokenRepository;

	@Before
	public void setUp() {
		User user1 = new User("user1", "pass");
		User user2 = new User("user2", "pass");

		userRepository.save(user1);
		userRepository.save(user2);

		todoRepository.save(new Todo("text1", user1));
		todoRepository.save(new Todo("text2", user1));
		todoRepository.save(new Todo("text1", user2));
		todoRepository.save(new Todo("text2", user2));

		tokenRepository.save(new RefreshToken(user1, "token1"));
		tokenRepository.save(new RefreshToken(user1, "token2"));
		tokenRepository.save(new RefreshToken(user1, "token3"));
		tokenRepository.save(new RefreshToken(user2, "token4"));
		tokenRepository.save(new RefreshToken(user2, "token5"));
	}

	@Test
	public void getUserByName() {
		Assert.assertFalse(userRepository.findByUsername("ogierke").isPresent());
		Assert.assertTrue(userRepository.findByUsername("user1").isPresent());
	}

	@Test
	public void getAllNotesByUsername() {
		List<Todo> todos = todoRepository.getAllByUserUsername("user1");
		Assert.assertEquals(2, todos.size());
	}

	@Test
	public void findByTokenThanDelete() {
		RefreshToken refreshToken = tokenRepository.findByToken("token2");
		Assert.assertEquals("user1", refreshToken.getUser().getUsername());

		tokenRepository.delete(refreshToken.getId());

		refreshToken = tokenRepository.findByToken("token2");
		Assert.assertNull(refreshToken);
	}

	@Test
	public void deleteAllByUsernameThanDeleteAll() {
		RefreshToken refreshToken = tokenRepository.findByToken("token1");
		Assert.assertNotNull(refreshToken);

		tokenRepository.deleteAllByUserUsername(refreshToken.getUser().getUsername());

		refreshToken = tokenRepository.findByToken("token1");
		Assert.assertNull(refreshToken);

		refreshToken = tokenRepository.findByToken("token5");
		Assert.assertNotNull(refreshToken);

		tokenRepository.deleteAll();

		refreshToken = tokenRepository.findByToken("token5");
		Assert.assertNull(refreshToken);
	}
}