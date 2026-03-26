package kpi.mayfff.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GenreDto {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 40, message = "Title must be between 3 and 40 characters")
    String title;

    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters")
    String description;
}
