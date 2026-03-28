package kpi.mayfff.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpi.mayfff.bookstore.AbstractIntegrationTest;
import kpi.mayfff.bookstore.dto.BookRequestDto;
import kpi.mayfff.bookstore.entity.BookEntity;
import kpi.mayfff.bookstore.entity.GenreEntity;
import kpi.mayfff.bookstore.repository.BookRepository;
import kpi.mayfff.bookstore.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Book Сontroller Integration Tests")
class BookControllerIntegrationTest extends AbstractIntegrationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void cleanUp() {
        bookRepository.deleteAll();
        genreRepository.deleteAll();
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        GenreEntity genre = createGenre("Fantasy", "Fantasy books");
        bookRepository.save(new BookEntity(null, "Dune", "Frank Herbert", "Sci-fi classic", genre, 19.99));
        bookRepository.save(new BookEntity(null, "Hyperion", "Dan Simmons", "Space opera", genre, 17.50));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Dune"))
                .andExpect(jsonPath("$[0].author").value("Frank Herbert"))
                .andExpect(jsonPath("$[0].genre.title").value("Fantasy"))
                .andExpect(jsonPath("$[1].title").value("Hyperion"));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        GenreEntity genre = createGenre("Mystery", "Mystery books");
        BookEntity book = bookRepository.save(
                new BookEntity(null, "Gone Girl", "Gillian Flynn", "Thriller novel", genre, 14.99)
        );

        mockMvc.perform(get("/api/v1/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Gone Girl"))
                .andExpect(jsonPath("$.author").value("Gillian Flynn"))
                .andExpect(jsonPath("$.description").value("Thriller novel"))
                .andExpect(jsonPath("$.genre.title").value("Mystery"))
                .andExpect(jsonPath("$.price").value(14.99));
    }

    @Test
    void shouldReturn404WhenBookDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/books/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Book with id 999 not found"));
    }

    @Test
    void shouldReturnBooksByGenre() throws Exception {
        GenreEntity fantasy = createGenre("Fantasy", "Fantasy books");
        GenreEntity sciFi = createGenre("Sci-Fi", "Science fiction");
        bookRepository.save(new BookEntity(null, "The Hobbit", "J.R.R. Tolkien", "Adventure", fantasy, 12.50));
        bookRepository.save(new BookEntity(null, "Neuromancer", "William Gibson", "Cyberpunk", sciFi, 15.00));

        mockMvc.perform(get("/api/v1/books/genre/{id}", fantasy.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("The Hobbit"))
                .andExpect(jsonPath("$[0].genre.title").value("Fantasy"));
    }

    @Test
    void shouldCreateBook() throws Exception {
        GenreEntity genre = createGenre("Drama", "Drama books");
        BookRequestDto bookRequestDto = bookRequestDto(
                "Hamlet",
                "William Shakespeare",
                "A tragedy play",
                genre.getId(),
                11.25
        );

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Hamlet"))
                .andExpect(jsonPath("$.author").value("William Shakespeare"))
                .andExpect(jsonPath("$.genre.title").value("Drama"))
                .andExpect(jsonPath("$.price").value(11.25));

        assertThat(bookRepository.findAll())
                .extracting(BookEntity::getTitle)
                .containsExactly("Hamlet");
    }

    @Test
    void shouldRejectInvalidBookPayload() throws Exception {
        BookRequestDto bookRequestDto = bookRequestDto("", "AB", "bad", null, null);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateBook() throws Exception {
        GenreEntity oldGenre = createGenre("Classic", "Classic books");
        GenreEntity newGenre = createGenre("History", "History books");
        BookEntity book = bookRepository.save(
                new BookEntity(null, "Old title", "Old author", "Old description", oldGenre, 8.99)
        );
        BookRequestDto bookRequestDto = bookRequestDto(
                "New title",
                "New author",
                "New description",
                newGenre.getId(),
                18.75
        );

        mockMvc.perform(put("/api/v1/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("New title"))
                .andExpect(jsonPath("$.author").value("New author"))
                .andExpect(jsonPath("$.genre.title").value("History"))
                .andExpect(jsonPath("$.price").value(18.75));

        BookEntity updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(updatedBook.getTitle()).isEqualTo("New title");
        assertThat(updatedBook.getAuthor()).isEqualTo("New author");
        assertThat(updatedBook.getDescription()).isEqualTo("New description");
        assertThat(updatedBook.getGenre().getId()).isEqualTo(newGenre.getId());
        assertThat(updatedBook.getPrice()).isEqualTo(18.75);
    }

    @Test
    void shouldDeleteBook() throws Exception {
        GenreEntity genre = createGenre("Horror", "Horror books");
        BookEntity book = bookRepository.save(
                new BookEntity(null, "It", "Stephen King", "Horror novel", genre, 13.40)
        );

        mockMvc.perform(delete("/api/v1/books/{id}", book.getId()))
                .andExpect(status().isOk());

        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }

    private GenreEntity createGenre(String title, String description) {
        return genreRepository.save(new GenreEntity(null, title, description));
    }

    private BookRequestDto bookRequestDto(
            String title,
            String author,
            String description,
            Long genreId,
            Double price
    ) {
        return BookRequestDto.builder()
                .title(title)
                .author(author)
                .description(description)
                .genreId(genreId)
                .price(price)
                .build();
    }
}
