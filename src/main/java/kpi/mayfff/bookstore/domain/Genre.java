package kpi.mayfff.bookstore.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    private Long id;
    private String title;
    private String description;
}
