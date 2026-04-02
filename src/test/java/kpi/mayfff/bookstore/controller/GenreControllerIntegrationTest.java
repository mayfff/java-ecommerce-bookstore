package kpi.mayfff.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpi.mayfff.bookstore.AbstractIntegrationTest;
import kpi.mayfff.bookstore.dto.GenreDto;
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

@DisplayName("Genre Controller Integration Tests")
class GenreControllerIntegrationTest extends AbstractIntegrationTest {
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
    void shouldReturnAllGenres() throws Exception {
        genreRepository.save(new GenreEntity(null, "Fantasy", "Fantasy books"));
        genreRepository.save(new GenreEntity(null, "Sci-Fi", "Science fiction"));

        mockMvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Fantasy"))
                .andExpect(jsonPath("$[0].description").value("Fantasy books"))
                .andExpect(jsonPath("$[1].title").value("Sci-Fi"));
    }

    @Test
    void shouldReturnGenreById() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "Mystery", "Mystery books"));

        mockMvc.perform(get("/api/v1/genres/{id}", genre.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Mystery"))
                .andExpect(jsonPath("$.description").value("Mystery books"));
    }

    @Test
    void shouldReturn404WhenGenreDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/genres/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Genre with id 999 not found"));
    }

    @Test
    void shouldCreateGenre() throws Exception {
        GenreDto genreDto = genreDto("Drama", "Drama books");

        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Drama"))
                .andExpect(jsonPath("$.description").value("Drama books"));

        assertThat(genreRepository.findAll())
                .extracting(GenreEntity::getTitle)
                .containsExactly("Drama");
    }

    @Test
    void shouldRejectInvalidPayload() throws Exception {
        GenreDto genreDto = genreDto("", "ab");

        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateGenre() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "Old title", "Old description"));
        GenreDto genreDto = genreDto("New title", "New description");

        mockMvc.perform(put("/api/v1/genres/{id}", genre.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("New title"))
                .andExpect(jsonPath("$.description").value("New description"));

        GenreEntity updatedGenre = genreRepository.findById(genre.getId()).orElseThrow();
        assertThat(updatedGenre.getTitle()).isEqualTo("New title");
        assertThat(updatedGenre.getDescription()).isEqualTo("New description");
    }

    @Test
    void shouldDeleteGenre() throws Exception {
        GenreEntity genre = genreRepository.save(new GenreEntity(null, "Horror", "Horror books"));

        mockMvc.perform(delete("/api/v1/genres/{id}", genre.getId()))
                .andExpect(status().isOk());

        assertThat(genreRepository.findById(genre.getId())).isEmpty();
    }

    private GenreDto genreDto(String title, String description) {
        return GenreDto.builder()
                .title(title)
                .description(description)
                .build();
    }
}
