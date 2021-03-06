package com.todo.service;

import com.todo.entity.User;
import com.todo.exception.NotFoundException;
import com.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getUserIfPresent(String username) {
		Optional<User> optionalUser = userRepository.findByUsername(username);

		if (!optionalUser.isPresent())
			throw new NotFoundException("User with current username is not found");

		return optionalUser.get();
	}
}
