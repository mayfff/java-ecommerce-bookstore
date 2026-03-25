package kpi.mayfff.bookstore.controller;

import jakarta.validation.Valid;
import kpi.mayfff.bookstore.dto.BookRequestDto;
import kpi.mayfff.bookstore.dto.BookResponseDto;
import kpi.mayfff.bookstore.service.BookService;
import kpi.mayfff.bookstore.service.mappers.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Validated
@RequiredArgsConstructor
public class BookController {
    BookService bookService;
    BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> books() {
        return ResponseEntity.ok(bookMapper.toBookResponseDtoList(bookService.getAllBooks()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> bookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookMapper.toBookResponseDto(bookService.getBookById(id)));
    }

    @GetMapping("genre/{id}")
    public ResponseEntity<List<BookResponseDto>> booksByGenre(@PathVariable Long id) {
        return ResponseEntity.ok(bookMapper.toBookResponseDtoList(bookService.getAllBooksByGenre(id)));
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> addBook(@RequestBody @Valid BookRequestDto bookRequestDto) {
        return ResponseEntity.ok(bookMapper.toBookResponseDto(bookService.createBook(bookRequestDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@RequestBody @Valid BookRequestDto bookRequestDto, @PathVariable Long id) {
        return ResponseEntity.ok(bookMapper.toBookResponseDto(bookService.updateBook(bookRequestDto, id)));
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
