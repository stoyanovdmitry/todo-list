package com.todo.controller;

import com.todo.entity.Todo;
import com.todo.entity.User;
import com.todo.repository.TodoRepository;
import com.todo.util.TodoUtil;
import com.todo.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{username}/todos")
public class TodoController {

	private final TodoRepository todoRepository;

	private final UserUtil userUtil;
	private final TodoUtil todoUtil;

	@Autowired
	public TodoController(TodoRepository todoRepository, UserUtil userUtil, TodoUtil todoUtil) {
		this.todoRepository = todoRepository;
		this.userUtil = userUtil;
		this.todoUtil = todoUtil;
	}

	@RequestMapping(method = RequestMethod.POST)
	public void addTodo(@PathVariable String username,
						@RequestBody Todo todo) {
		User user = userUtil.getUserIfPresent(username);

		todo.setUser(user);
		todoRepository.save(todo);
	}

	@RequestMapping(value = "/{id}",
			method = RequestMethod.DELETE)
	public void deleteTodo(@PathVariable int id,
						   @PathVariable String username) {
		todoUtil.getTodoIfValid(id, username);
		todoRepository.delete(id);
	}

	@RequestMapping(value = "/{id}",
			method = RequestMethod.PUT)
	public void updateTodo(@PathVariable int id,
						   @PathVariable String username,
						   @RequestBody Todo todo) {
		Todo existTodo = todoUtil.getTodoIfValid(id, username);

		existTodo.setText(todo.getText());
		existTodo.setCompleted(todo.isCompleted());

		todoRepository.save(existTodo);
	}

	@RequestMapping(value = "/{id}",
			method = RequestMethod.GET)
	public Todo getById(@PathVariable int id,
						@PathVariable String username) {
		return todoUtil.getTodoIfValid(id, username);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Todo> getAllByUsername(@PathVariable String username) {
		return todoRepository.getAllByUserUsername(username);
	}
}
