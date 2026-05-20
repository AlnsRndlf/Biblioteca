package cl.duocuc.penalty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyRequestDto {

    @NotBlank(message = "el rut es obligatirio")
    private String userRut;

    @NotNull(message = "la multa tiene que tener monto")
    private Integer amount;

    @NotBlank(message = "tiene que haber motivo de multa")
    private String reason;
}
