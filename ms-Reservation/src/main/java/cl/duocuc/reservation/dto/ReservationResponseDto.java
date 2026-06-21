package cl.duocuc.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {

    private Long idReservation;
    private String userRut;
    private Long bookIsbn;
    private LocalDate reservationDate;
    private String status;
}
