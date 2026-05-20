package cl.duocuc.reservation.service;

import cl.duocuc.reservation.dto.ReservationRequestDto;
import cl.duocuc.reservation.dto.ReservationResponseDto;

import java.util.List;

public interface IReservationService {

    ReservationResponseDto save(ReservationRequestDto request);
    List<ReservationResponseDto> findAll();
    ReservationResponseDto findById(Long id);
    ReservationResponseDto updateStatus(Long idReservation, String status);

}
