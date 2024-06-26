package com.demo.todo.repository;

import com.demo.todo.config.DatasourceConfiguration;
import com.demo.todo.domain.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(showSql = true, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ContextConfiguration(classes = DatasourceConfiguration.class)
class TodoRepositoryTests {
    @Autowired
    private TodoRepository repository;

    @Test
    void shouldPersistANewTodo() {
        Todo todo = new Todo("Movida");

        Todo persisted = this.repository.save(todo);

        assertNotNull(persisted.getId());
        assertEquals(todo.getDescription(), persisted.getDescription());
    }

    @Test
    void shouldRetrieveAllTodos() {
        Todo todo = new Todo("movido");
        Todo another = new Todo("otra movida");

        this.repository.save(todo);
        this.repository.save(another);

        assertEquals(2, this.repository.findAll().size());
    }
}
