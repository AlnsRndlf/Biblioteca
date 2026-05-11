package cl.duocucUser.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@Data @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UserDto {


    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$")
    private String rut;

    @NotBlank(message = "nombre completo no puede estar vacio")
    @Size(min = 3, max = 255,message = "nombre completo debe tener entre 3 y 255 caracteres")
    private String fullName;

    @NotBlank(message = "email no puede estar vacio")
    @Email(message = "cuek. formato de email incorrecto")
    private String email;

    @NotNull(message = "la fehca no puede estar vacia")
    @PastOrPresent(message = "la fecha todavia no ocurre")
    private LocalDate membershipedDate;

}
