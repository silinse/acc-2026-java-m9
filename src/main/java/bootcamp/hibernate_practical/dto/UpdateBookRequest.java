package bootcamp.hibernate_practical.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateBookRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String genre;

    @Min(1)
    private int publicationYear;

    @NotNull
    private Boolean available;
}
