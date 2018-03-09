package com.todo.service;

import com.todo.entity.Todo;
import com.todo.entity.User;
import com.todo.exception.NotFoundException;
import com.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

	@Autowired
	private UserService userService;

	@Autowired
	private TodoRepository todoRepository;

	public Todo getTodoIfValid(int id, String username) {
		User user = userService.getUserIfPresent(username);
		Todo todo = todoRepository.getOne(id);

		if (user != todo.getUser())
			throw new NotFoundException();

		return todo;
	}
}
