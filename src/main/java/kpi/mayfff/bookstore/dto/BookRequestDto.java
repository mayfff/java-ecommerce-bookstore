package kpi.mayfff.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BookRequestDto {
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    String title;

    @NotBlank(message = "Author is required")
    @Size(min = 3, max = 50, message = "Author must be between 3 and 50 characters")
    String author;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    String description;

    @NotBlank(message = "Genre is required")
    Long genreId;

    @NotBlank(message = "Price is required")
    Double price;
}
