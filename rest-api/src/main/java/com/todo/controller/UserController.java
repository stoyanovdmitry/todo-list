package com.todo.controller;

import com.todo.entity.User;
import com.todo.exception.NotFoundException;
import com.todo.exception.ConflictException;
import com.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/{username}")
	public User getUserByUsername(@PathVariable String username) {
		return getIfPresent(username);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void registerUser(@RequestBody User user) {
		try {
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new ConflictException();
		}
	}

	private User getIfPresent(String username) {
		Optional<User> optionalUser = userRepository.findByUsername(username);

		if (!optionalUser.isPresent())
			throw new NotFoundException();

		return optionalUser.get();
	}
}
