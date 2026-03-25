package kpi.mayfff.bookstore.repository;

import kpi.mayfff.bookstore.entity.GenreEntity;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<GenreEntity, Long> {
}
