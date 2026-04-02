package kpi.mayfff.bookstore.service.mappers;

import kpi.mayfff.bookstore.domain.Genre;
import kpi.mayfff.bookstore.dto.GenreDto;
import kpi.mayfff.bookstore.entity.GenreEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreEntity toGenreEntity(Genre genre);
    Genre toGenre(GenreEntity genreEntity);
    List<GenreDto> toGenreDtoList(List<Genre> genres);
    GenreDto toGenreDto(Genre genreById);
    List<Genre> toGenreList(Iterable<GenreEntity> genres);
}
