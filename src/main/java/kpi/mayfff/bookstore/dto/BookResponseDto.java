package kpi.mayfff.bookstore.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BookResponseDto {
    Long id;
    String title;
    String author;
    String description;
    GenreDto genre;
    Double price;
}
