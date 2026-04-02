package kpi.mayfff.bookstore.controller;

import jakarta.validation.Valid;
import kpi.mayfff.bookstore.dto.GenreDto;
import kpi.mayfff.bookstore.service.GenreService;
import kpi.mayfff.bookstore.service.mappers.GenreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/genres")
public class GenreController {
    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @GetMapping
    public ResponseEntity<List<GenreDto>> genres() {
        return ResponseEntity.ok(genreMapper.toGenreDtoList(genreService.getAllGenres()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> genreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreMapper.toGenreDto(genreService.getGenreById(id)));
    }

    @PostMapping
    public ResponseEntity<GenreDto> addGenre(@RequestBody @Valid GenreDto genreDto) {
        return ResponseEntity.ok(genreMapper.toGenreDto(genreService.createGenre(genreDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(@RequestBody @Valid GenreDto genreDto, @PathVariable Long id) {
        return ResponseEntity.ok(genreMapper.toGenreDto(genreService.updateGenre(genreDto, id)));
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }
}
