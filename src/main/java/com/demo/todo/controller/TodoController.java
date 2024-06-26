package com.demo.todo.controller;

import com.demo.todo.domain.Todo;
import com.demo.todo.model.TodoRequest;
import com.demo.todo.repository.TodoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Todo> save(@Valid @RequestBody TodoRequest todo) {
        return ResponseEntity.ok(this.repository.save(new Todo(todo.getDescription())));
    }

    @GetMapping
    public ResponseEntity<List<Todo>> findAll() {
        return ResponseEntity.ok(this.repository.findAll());
    }
}
