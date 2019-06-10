package com.todo.controller;

import com.todo.entity.Todo;
import com.todo.entity.User;
import com.todo.repository.TodoRepository;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{username}/todos")
public class TodoController {

    private final TodoRepository todoRepository;

    private final UserService userService;
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoRepository todoRepository, UserService userService, TodoService todoService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
        this.todoService = todoService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Todo addTodo(@PathVariable String username,
                        @RequestBody Todo todo) {
        User user = userService.getUserIfPresent(username);

        todo.setUser(user);
        return todoRepository.save(todo);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE)
    public void deleteTodo(@PathVariable int id,
                           @PathVariable String username) {
        todoService.getTodoIfValid(id, username);
        todoRepository.delete(id);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT)
    public void updateTodo(@PathVariable int id,
                           @PathVariable String username,
                           @RequestBody Todo todo) {
        Todo existTodo = todoService.getTodoIfValid(id, username);

        existTodo.setText(todo.getText());
        existTodo.setCompleted(todo.isCompleted());

        todoRepository.save(existTodo);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET)
    public Todo getById(@PathVariable int id,
                        @PathVariable String username) {
        return todoService.getTodoIfValid(id, username);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Todo> getAllByUsername(@PathVariable String username) {
        return todoRepository.getAllByUserUsername(username);
    }
}
