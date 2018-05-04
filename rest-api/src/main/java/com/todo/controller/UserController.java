package com.todo.controller;

import com.todo.entity.User;
import com.todo.repository.UserRepository;
import com.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserRepository userRepository;

	private final UserService userService;

	@Autowired
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@RequestMapping(value = "/{username}",
			method = RequestMethod.GET)
	public User getUserByUsername(@PathVariable String username) {
		return userService.getUserIfPresent(username);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void registerUser(@RequestBody User user) {
		userRepository.save(user);
	}
}
