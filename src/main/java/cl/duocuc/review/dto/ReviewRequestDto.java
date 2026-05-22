package cl.duocuc.review.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @NotBlank(message = "el rut es obligatorio")
    private String userRut;

    @NotNull(message = "el isbn es obligatorio")
    private Long bookIsbn;

    @NotNull(message = "la calificacion es obligatoria")
    @Min(value = 1,message = "tiene q estar entre 1 y 5")
    @Max(value = 1,message = "tiene q estar entre 1 y 5")
    private Integer rating;

    @Size(max = 500, message = "el mensaje no puede tener mas de 500 caracteres")
    private String comment;
}
