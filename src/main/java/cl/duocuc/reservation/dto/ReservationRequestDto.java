package cl.duocuc.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {

    @NotBlank(message = "el rut es obligatorio")
    private String userRut;

    @NotNull(message = "el isbn es obligatorio")
    private Long bookIsbn;
}
