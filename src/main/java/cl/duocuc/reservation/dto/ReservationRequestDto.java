package cl.duocuc.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {

    @NotBlank(message = "el rut es obligatorio")
    private String userRut;

    @NotBlank(message = "el isbn es obligatorio")
    private Long isbn;
}
