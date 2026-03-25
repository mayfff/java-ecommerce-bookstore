package kpi.mayfff.bookstore.service;

import kpi.mayfff.bookstore.domain.Book;
import kpi.mayfff.bookstore.dto.BookRequestDto;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book getBookById(Long id);
    List<Book> getAllBooksByGenre(Long genreId);
    Book createBook(BookRequestDto bookRequestDto);
    Book updateBook(BookRequestDto bookRequestDto, Long id);
    void deleteBook(Long id);
}
