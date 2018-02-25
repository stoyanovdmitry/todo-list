package com.todo;

import com.todo.entity.Todo;
import com.todo.entity.User;
import com.todo.repository.TodoRepository;
import com.todo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, TodoRepository todoRepository) {
		return (cmd) -> Arrays.asList(
				"user,admin".split(","))
							  .forEach(name -> {
							  	User user = userRepository.save(new User(name, "password"));
							  	todoRepository.save(new Todo("todo1", user));
							  	todoRepository.save(new Todo("todo2", user));
							  });
	}
}
