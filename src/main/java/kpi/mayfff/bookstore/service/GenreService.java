package kpi.mayfff.bookstore.service;

import jakarta.validation.Valid;
import kpi.mayfff.bookstore.domain.Genre;
import kpi.mayfff.bookstore.dto.GenreDto;

import java.util.List;

public interface GenreService {
    Genre getGenreById(Long id);
    List<Genre> getAllGenres();
    Genre createGenre(GenreDto genreDto);
    Genre updateGenre(GenreDto genreDto, Long id);
    void deleteGenre(Long id);
}
