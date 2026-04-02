package kpi.mayfff.bookstore.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private Long id;
    private String title;
    private String author;
    private String description;
    private Genre genre;
    private Double price;
}
