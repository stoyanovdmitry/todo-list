package com.todo.service;

import com.todo.entity.Todo;
import com.todo.entity.User;
import com.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    private UserService userService;

    @Autowired
    private TodoRepository todoRepository;

    public Todo getTodoIfValid(int id, String username) {
        User user = userService.getUserIfPresent(username);
        Todo todo = todoRepository.findOne(id);

        if (user != todo.getUser()) {
            throw new AccessDeniedException("You have no access to this resource");
        }

        return todo;
    }
}
