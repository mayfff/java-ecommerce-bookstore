package kpi.mayfff.bookstore.service;

import jakarta.persistence.PersistenceException;
import kpi.mayfff.bookstore.domain.Genre;
import kpi.mayfff.bookstore.dto.GenreDto;
import kpi.mayfff.bookstore.entity.GenreEntity;
import kpi.mayfff.bookstore.repository.GenreRepository;
import kpi.mayfff.bookstore.service.exception.GenreNotFoundException;
import kpi.mayfff.bookstore.service.impl.GenreServiceImpl;
import kpi.mayfff.bookstore.service.mappers.GenreMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Genre Service Tests")
@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    private GenreDto genreDto;
    private GenreEntity genreEntity;
    private Genre genre;

    @BeforeEach
    void setUp() {
        genreDto = GenreDto.builder()
                .title("Fantasy")
                .description("Fantasy books")
                .build();

        genreEntity = new GenreEntity(1L, "Fantasy", "Fantasy books");

        genre = Genre.builder()
                .id(1L)
                .title("Fantasy")
                .description("Fantasy books")
                .build();
    }

    @Test
    void shouldGetAllGenres() {
        List<GenreEntity> genreEntities = List.of(genreEntity);
        List<Genre> genres = List.of(genre);

        when(genreRepository.findAll()).thenReturn(genreEntities);
        when(genreMapper.toGenreList(genreEntities)).thenReturn(genres);

        List<Genre> result = genreService.getAllGenres();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fantasy", result.getFirst().getTitle());
    }

    @Test
    void shouldGetGenreById() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(genreMapper.toGenre(genreEntity)).thenReturn(genre);

        Genre result = genreService.getGenreById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Fantasy", result.getTitle());
    }

    @Test
    void shouldGetGenreByIdNotFound() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreService.getGenreById(1L));
    }

    @Test
    void shouldCreateGenre() {
        when(genreMapper.toGenreEntity(any(Genre.class))).thenReturn(genreEntity);
        when(genreRepository.save(genreEntity)).thenReturn(genreEntity);
        when(genreMapper.toGenre(genreEntity)).thenReturn(genre);

        Genre result = genreService.createGenre(genreDto);

        assertNotNull(result);
        assertEquals("Fantasy", result.getTitle());
    }

    @Test
    void shouldUpdateGenre() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(genreRepository.save(genreEntity)).thenReturn(genreEntity);
        when(genreMapper.toGenre(genreEntity)).thenReturn(genre);

        Genre result = genreService.updateGenre(genreDto, 1L);

        assertNotNull(result);
        assertEquals("Fantasy", genreEntity.getTitle());
        assertEquals("Fantasy books", genreEntity.getDescription());
    }

    @Test
    void shouldUpdateGenreNotFound() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreService.updateGenre(genreDto, 1L));
    }

    @Test
    void shouldDeleteGenre() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(genreMapper.toGenre(genreEntity)).thenReturn(genre);
        when(genreMapper.toGenreEntity(genre)).thenReturn(genreEntity);

        genreService.deleteGenre(1L);

        verify(genreRepository, times(1)).delete(genreEntity);
    }

    @Test
    void shouldDeleteGenreNotFound() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreService.deleteGenre(1L));
    }

    @Test
    void shouldThrowPersistenceExceptionOnCreateGenre() {
        when(genreMapper.toGenreEntity(any(Genre.class))).thenReturn(genreEntity);
        when(genreRepository.save(genreEntity)).thenThrow(new RuntimeException("Database error"));

        assertThrows(PersistenceException.class, () -> genreService.createGenre(genreDto));
    }

    @Test
    void shouldThrowPersistenceExceptionOnUpdateGenre() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(genreRepository.save(genreEntity)).thenThrow(new RuntimeException("Database error"));

        assertThrows(PersistenceException.class, () -> genreService.updateGenre(genreDto, 1L));
    }

    @Test
    void shouldThrowPersistenceExceptionOnDeleteGenre() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genreEntity));
        when(genreMapper.toGenre(genreEntity)).thenReturn(genre);
        when(genreMapper.toGenreEntity(genre)).thenReturn(genreEntity);
        doThrow(new RuntimeException("Database error")).when(genreRepository).delete(genreEntity);

        assertThrows(PersistenceException.class, () -> genreService.deleteGenre(1L));
    }
}
