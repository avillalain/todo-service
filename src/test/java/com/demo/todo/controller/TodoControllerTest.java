package com.demo.todo.controller;

import com.demo.todo.domain.Todo;
import com.demo.todo.model.TodoRequest;
import com.demo.todo.repository.TodoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @MockBean
    private TodoRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnNewTodo() throws Exception {
        TodoRequest request = new TodoRequest("some-description");
        Todo expected = new Todo(1L, "request");
        when(repository.save(any())).thenReturn(expected);

        this.mockMvc.perform(post("/todos")
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.description").value(expected.getDescription()));
    }

    @Test
    void shouldReturnBadRequestWithEmptyDescription() throws Exception {
        TodoRequest request = new TodoRequest("");
        this.mockMvc.perform(post("/todos")
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWithBlankDescription() throws Exception {
        TodoRequest request = new TodoRequest("     ");
        this.mockMvc.perform(post("/todos")
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAllTodos() throws Exception {

        List<Todo> todos = List.of(new Todo(1L, "movida"), new Todo(2L, "movida"));
        when(repository.findAll()).thenReturn(todos);

        this.mockMvc.perform(get("/todos")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(todos.size()));
    }
}
