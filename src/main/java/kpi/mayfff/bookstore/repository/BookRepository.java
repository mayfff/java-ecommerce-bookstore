package kpi.mayfff.bookstore.repository;

import kpi.mayfff.bookstore.entity.BookEntity;
import kpi.mayfff.bookstore.entity.GenreEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<BookEntity, Long> {
    Iterable<BookEntity> findAllByGenre(GenreEntity genreEntity);
}
