package com.todo.controller;

import com.todo.entity.User;
import com.todo.exception.ConflictException;
import com.todo.repository.UserRepository;
import com.todo.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserRepository userRepository;

	private final UserUtil userUtil;

	@Autowired
	public UserController(UserRepository userRepository, UserUtil userUtil) {
		this.userRepository = userRepository;
		this.userUtil = userUtil;
	}

	@RequestMapping("/{username}")
	public User getUserByUsername(@PathVariable String username) {
		return userUtil.getUserIfPresent(username);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void registerUser(@RequestBody User user) {
		try {
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new ConflictException();
		}
	}
}
