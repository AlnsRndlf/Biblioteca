package cl.duocucLoan.loan.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequestDto {

    @Size(min = 9, max = 14, message = "rut invalido")
    @NotBlank(message = "el rut es obligatorio")
    private String userRut;

    @Min(value = 1000000000000L, message = "el ISBN tiene que ser de 13 digitos.")
    @Max(value = 9999999999999L, message = "el ISBN tiene que ser de 13 digitos.")
    @NotNull(message = "el ISBN es obligatorio")
    private Long bookIsbn;

}
