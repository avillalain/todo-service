package com.demo.todo;

import com.demo.todo.config.DatasourceConfiguration;
import com.demo.todo.model.TodoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = DatasourceConfiguration.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class TodoServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    void shouldAcceptNewTodos() {
        String description = "movida";
        this.client.post().uri("/todos")
                .body(BodyInserters.fromValue(new TodoRequest(description)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.description").isEqualTo(description);
    }

    @Test
    void shouldReturnAllTodos() {
        this.client.post().uri("/todos")
                .body(BodyInserters.fromValue(new TodoRequest("movida")))
                .accept(APPLICATION_JSON)
                .exchange();
        this.client.post().uri("/todos")
                .body(BodyInserters.fromValue(new TodoRequest("movida1")))
                .accept(APPLICATION_JSON)
                .exchange();

        this.client.get().uri("/todos")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.size()").isEqualTo(2);
    }
}
