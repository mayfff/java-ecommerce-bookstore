package kpi.mayfff.bookstore.service.impl;

import jakarta.persistence.PersistenceException;
import kpi.mayfff.bookstore.domain.Genre;
import kpi.mayfff.bookstore.dto.GenreDto;
import kpi.mayfff.bookstore.entity.GenreEntity;
import kpi.mayfff.bookstore.repository.GenreRepository;
import kpi.mayfff.bookstore.service.GenreService;
import kpi.mayfff.bookstore.service.exception.GenreNotFoundException;
import kpi.mayfff.bookstore.service.mappers.GenreMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreMapper genreMapper;
    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(Long id) {
        return genreMapper.toGenre(genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        return genreMapper.toGenreList(genreRepository.findAll());
    }

    @Override
    @Transactional
    public Genre createGenre(GenreDto genreDto) {
        Genre genre = Genre.builder()
                .title(genreDto.getTitle())
                .description(genreDto.getDescription())
                .build();

        try {
            return genreMapper.toGenre(genreRepository.save(genreMapper.toGenreEntity(genre)));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public Genre updateGenre(GenreDto genreDto, Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));

        genreEntity.setTitle(genreDto.getTitle());
        genreEntity.setDescription(genreDto.getDescription());

        try {
            return genreMapper.toGenre(genreRepository.save(genreEntity));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = getGenreById(id);

        try {
            genreRepository.delete(genreMapper.toGenreEntity(genre));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
