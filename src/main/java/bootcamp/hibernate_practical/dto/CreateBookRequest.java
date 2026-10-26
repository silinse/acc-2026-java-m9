package bootcamp.hibernate_practical.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Getter
public class CreateBookRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String genre;

    @Min(1)
    private int publicationYear;
}
