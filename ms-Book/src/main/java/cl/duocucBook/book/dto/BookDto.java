package cl.duocucBook.book.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class BookDto {

    @Min(value = 1000000000000L, message = "el ISBN tiene que ser de 13 digitos.")
    @Max(value = 9999999999999L, message = "el ISBN tiene que ser de 13 digitos.")
    @NotNull(message = "El ISBN es obligatorio.")
    private Long isbn;

    @Size(min = 1,max = 255,message = "cantidad de caracteres invalida.")
    @NotBlank(message = "El titulo no puede estar vacio.")
    private String title;

    @Size(min = 1,max = 255,message = "cantidad de caracteres invalida.")
    @NotBlank(message = "Tiene que tener un autor.")
    private String author;

    // se puede desconocer el año
    private Integer yearPublicated;

    @NotNull
    @Min(value = 0, message = "el stock disponible no puede ser un valor negativo.")
    private Integer stock;
}
