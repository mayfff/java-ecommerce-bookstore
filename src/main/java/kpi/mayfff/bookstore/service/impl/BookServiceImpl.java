package kpi.mayfff.bookstore.service.impl;

import jakarta.persistence.PersistenceException;
import kpi.mayfff.bookstore.domain.Book;
import kpi.mayfff.bookstore.domain.Genre;
import kpi.mayfff.bookstore.dto.BookRequestDto;
import kpi.mayfff.bookstore.entity.BookEntity;
import kpi.mayfff.bookstore.entity.GenreEntity;
import kpi.mayfff.bookstore.repository.BookRepository;
import kpi.mayfff.bookstore.service.BookService;
import kpi.mayfff.bookstore.service.GenreService;
import kpi.mayfff.bookstore.service.exception.BookNotFoundException;
import kpi.mayfff.bookstore.service.mappers.BookMapper;
import kpi.mayfff.bookstore.service.mappers.GenreMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    BookMapper bookMapper;
    GenreMapper genreMapper;
    BookRepository bookRepository;
    GenreService genreService;

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookMapper.toBookList(bookRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookMapper.toBook(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooksByGenre(Long genreId) {
        GenreEntity genre = genreMapper.toGenreEntity(genreService.getGenreById(genreId));
        return bookMapper.toBookList(bookRepository.findAllByGenre(genre));
    }

    @Override
    @Transactional
    public Book createBook(BookRequestDto bookRequestDto) {
        Genre genre = genreService.getGenreById(bookRequestDto.getGenreId());

        Book newBook = Book.builder()
                .title(bookRequestDto.getTitle())
                .author(bookRequestDto.getAuthor())
                .description(bookRequestDto.getDescription())
                .genre(genre)
                .price(bookRequestDto.getPrice())
                .build();
        try {
            return bookMapper.toBook(bookRepository.save(bookMapper.toBookEntity(newBook)));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public Book updateBook(BookRequestDto bookRequestDto, Long id) {
        BookEntity book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        GenreEntity genre = genreMapper.toGenreEntity(genreService.getGenreById(bookRequestDto.getGenreId()));

        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setDescription(bookRequestDto.getDescription());
        book.setGenre(genre);
        book.setPrice(bookRequestDto.getPrice());

        try {
            return bookMapper.toBook(bookRepository.save(book));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);

        try {
            bookRepository.delete(bookMapper.toBookEntity(book));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
