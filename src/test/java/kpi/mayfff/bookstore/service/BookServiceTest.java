package kpi.mayfff.bookstore.service;

import jakarta.persistence.PersistenceException;
import kpi.mayfff.bookstore.domain.Book;
import kpi.mayfff.bookstore.domain.Genre;
import kpi.mayfff.bookstore.dto.BookRequestDto;
import kpi.mayfff.bookstore.entity.BookEntity;
import kpi.mayfff.bookstore.entity.GenreEntity;
import kpi.mayfff.bookstore.repository.BookRepository;
import kpi.mayfff.bookstore.service.exception.BookNotFoundException;
import kpi.mayfff.bookstore.service.impl.BookServiceImpl;
import kpi.mayfff.bookstore.service.mappers.BookMapper;
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

@DisplayName("Book Service Tests")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookRequestDto bookRequestDto;
    private Genre genre;
    private GenreEntity genreEntity;
    private BookEntity bookEntity;
    private Book book;

    @BeforeEach
    void setUp() {
        bookRequestDto = BookRequestDto.builder()
                .title("Dune")
                .author("Frank Herbert")
                .description("Science fiction classic")
                .genreId(1L)
                .price(19.99)
                .build();

        genre = Genre.builder()
                .id(1L)
                .title("Sci-Fi")
                .description("Science fiction books")
                .build();

        genreEntity = new GenreEntity(1L, "Sci-Fi", "Science fiction books");

        bookEntity = new BookEntity(
                1L,
                "Dune",
                "Frank Herbert",
                "Science fiction classic",
                genreEntity,
                19.99
        );

        book = Book.builder()
                .id(1L)
                .title("Dune")
                .author("Frank Herbert")
                .description("Science fiction classic")
                .genre(genre)
                .price(19.99)
                .build();
    }

    @Test
    void shouldGetAllBooks() {
        List<BookEntity> bookEntities = List.of(bookEntity);
        List<Book> books = List.of(book);

        when(bookRepository.findAll()).thenReturn(bookEntities);
        when(bookMapper.toBookList(bookEntities)).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dune", result.getFirst().getTitle());
    }

    @Test
    void shouldGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.toBook(bookEntity)).thenReturn(book);

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dune", result.getTitle());
    }

    @Test
    void shouldGetBookByIdNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void shouldGetAllBooksByGenre() {
        List<BookEntity> bookEntities = List.of(bookEntity);
        List<Book> books = List.of(book);

        when(genreService.getGenreById(1L)).thenReturn(genre);
        when(genreMapper.toGenreEntity(genre)).thenReturn(genreEntity);
        when(bookRepository.findAllByGenre(genreEntity)).thenReturn(bookEntities);
        when(bookMapper.toBookList(bookEntities)).thenReturn(books);

        List<Book> result = bookService.getAllBooksByGenre(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dune", result.getFirst().getTitle());
    }

    @Test
    void shouldCreateBook() {
        when(genreService.getGenreById(1L)).thenReturn(genre);
        when(bookMapper.toBookEntity(any(Book.class))).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(bookMapper.toBook(bookEntity)).thenReturn(book);

        Book result = bookService.createBook(bookRequestDto);

        assertNotNull(result);
        assertEquals("Dune", result.getTitle());
    }

    @Test
    void shouldUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(genreService.getGenreById(1L)).thenReturn(genre);
        when(genreMapper.toGenreEntity(genre)).thenReturn(genreEntity);
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(bookMapper.toBook(bookEntity)).thenReturn(book);

        Book result = bookService.updateBook(bookRequestDto, 1L);

        assertNotNull(result);
        assertEquals("Dune", bookEntity.getTitle());
        assertEquals("Frank Herbert", bookEntity.getAuthor());
        assertEquals("Science fiction classic", bookEntity.getDescription());
        assertEquals(19.99, bookEntity.getPrice());
        assertEquals(genreEntity, bookEntity.getGenre());
    }

    @Test
    void shouldUpdateBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookRequestDto, 1L));
    }

    @Test
    void shouldDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.toBook(bookEntity)).thenReturn(book);
        when(bookMapper.toBookEntity(book)).thenReturn(bookEntity);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(bookEntity);
    }

    @Test
    void shouldDeleteBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
    }

    @Test
    void shouldThrowPersistenceExceptionOnCreateBook() {
        when(genreService.getGenreById(1L)).thenReturn(genre);
        when(bookMapper.toBookEntity(any(Book.class))).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenThrow(new RuntimeException("Database error"));

        assertThrows(PersistenceException.class, () -> bookService.createBook(bookRequestDto));
    }

    @Test
    void shouldThrowPersistenceExceptionOnUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(genreService.getGenreById(1L)).thenReturn(genre);
        when(genreMapper.toGenreEntity(genre)).thenReturn(genreEntity);
        when(bookRepository.save(bookEntity)).thenThrow(new RuntimeException("Database error"));

        assertThrows(PersistenceException.class, () -> bookService.updateBook(bookRequestDto, 1L));
    }

    @Test
    void shouldThrowPersistenceExceptionOnDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.toBook(bookEntity)).thenReturn(book);
        when(bookMapper.toBookEntity(book)).thenReturn(bookEntity);
        doThrow(new RuntimeException("Database error")).when(bookRepository).delete(bookEntity);

        assertThrows(PersistenceException.class, () -> bookService.deleteBook(1L));
    }
}
