package com.todo.test;

import com.todo.entity.Todo;
import com.todo.entity.User;
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
}