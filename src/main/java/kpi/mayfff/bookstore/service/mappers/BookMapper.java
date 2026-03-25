package kpi.mayfff.bookstore.service.mappers;

import kpi.mayfff.bookstore.domain.Book;
import kpi.mayfff.bookstore.dto.BookResponseDto;
import kpi.mayfff.bookstore.entity.BookEntity;
import org.mapstruct.Mapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookEntity toBookEntity(Book book);
    List<Book> toBookList(Iterable<BookEntity> bookEntityList);
    Book toBook(BookEntity bookEntity);
    List<BookResponseDto> toBookResponseDtoList(List<Book> allBooks);
    BookResponseDto toBookResponseDto(Book bookById);
}
