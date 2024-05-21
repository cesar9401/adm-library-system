package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmBook;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdmBookControllerTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @WithMockUser
    public void createWithRepeatedIsbn() throws Exception {
        final var isbn = "978-1682852477";
        final var book = new AdmBook();
        book.setIsbn(isbn);
        book.setTitle("Computer Sciencie: Concepts and Applications");
        book.setAuthor("Tom Halt");
        book.setEditorial("Willford Press");
        book.setStock(10);
        book.setPublicationDate(LocalDate.of(2016, 5, 27));

        final var otherBook = new AdmBook();
        otherBook.setIsbn(isbn);
        otherBook.setTitle("Harry Potter");
        otherBook.setAuthor("J.K. Rowling");
        otherBook.setEditorial("J.K. Rowling");
        otherBook.setStock(10);
        otherBook.setPublicationDate(LocalDate.of(1997, 1, 1));

        mockMvc.perform(
                        post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(book))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").isNumber());

        mockMvc.perform(
                        post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(otherBook))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("isbn_already_exists"));
    }

    @Test
    @WithMockUser
    public void shouldReturnNotFoundWhenUpdateBookThatDoesntExist() throws Exception {
        final var bookId = -1L;
        final var book = new AdmBook();
        book.setBookId(bookId);
        book.setIsbn("978-1234567890");
        book.setTitle("Demo Book");
        book.setAuthor("Demo Author");
        book.setEditorial("Demo Editorial");
        book.setStock(10);
        book.setPublicationDate(LocalDate.of(2020, 1, 1));

        mockMvc.perform(
                        put("/books/{bookId}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(book))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("book_not_found"));
    }

    @Test
    @WithMockUser
    public void updateBookTest() throws Exception {
        final var isbn = "123-0987654321";
        final var newTitle = "A new book";

        final var book = new AdmBook();
        book.setIsbn(isbn);
        book.setTitle("Book demo");
        book.setAuthor("Author demo");
        book.setEditorial("Editorial demo");
        book.setStock(1);
        book.setPublicationDate(LocalDate.of(2024, 1, 1));

        var result = mockMvc.perform(
                        post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(book))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        var json = result.getResponse().getContentAsString();
        var createdBook = mapper.readValue(json, AdmBook.class);

        assertAll(
                () -> assertNotNull(createdBook),
                () -> assertNotNull(createdBook.getBookId()),
                () -> assertEquals(isbn, createdBook.getIsbn())
        );

        var bookId = createdBook.getBookId();
        // find by id
        var resultById = mockMvc.perform(
                        get("/books/{bookId}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var bookById = mapper.readValue(resultById.getResponse().getContentAsString(), AdmBook.class);
        assertAll(
                () -> assertNotNull(bookById),
                () -> assertEquals(isbn, bookById.getIsbn()),
                () -> assertEquals(bookId, bookById.getBookId())
        );

        final var newStock = bookById.getStock() + 2;
        bookById.setTitle(newTitle);
        bookById.setStock(newStock);

        mockMvc.perform(
                        put("/books/{bookId}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(bookById))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.stock").value(newStock))
                .andExpect(jsonPath("$.bookId").value(bookId));
    }

    @Test
    @WithMockUser
    public void findAllTest() throws Exception {
        final var page = 1;
        final var size = 20;

        mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"));
    }
}
