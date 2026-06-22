package cl.duocuc.reservation.service;


import cl.duocuc.reservation.client.BookClient;
import cl.duocuc.reservation.client.UserClient;
import cl.duocuc.reservation.dto.external.BookResponseDto;
import cl.duocuc.reservation.dto.ReservationRequestDto;
import cl.duocuc.reservation.dto.ReservationResponseDto;
import cl.duocuc.reservation.dto.external.UserResponseDto;
import cl.duocuc.reservation.model.Reservation;
import cl.duocuc.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository repository;
    private final UserClient userClient;
    private final BookClient bookClient;

    private ReservationResponseDto toDto(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getIdReservation(),
                reservation.getUserRut(),
                reservation.getBookIsbn(),
                reservation.getReservationDate(),
                reservation.getStatus()
        );
    }

    private Reservation toEntity(ReservationRequestDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setUserRut(reservationDto.getUserRut());
        reservation.setBookIsbn(reservationDto.getBookIsbn());
        return reservation;
    }

    @Override
    public ReservationResponseDto save(ReservationRequestDto request) {
        UserResponseDto userDto = userClient.getUserByRut(request.getUserRut());
        if (userDto == null) {
            throw new RuntimeException("Usuario no encontrado en el sistema.");
        }
        BookResponseDto bookDto = bookClient.getBookByIsbn(request.getBookIsbn());
        if (bookDto == null) {
            throw new RuntimeException("Libro no encontrado en el sistema.");
        }
        if (bookDto.getStock() > 0) {
          throw new RuntimeException("hay disponibles para retirar.");
        }
        Reservation reservation = toEntity(request);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("pendiente");
        Reservation savedReservation = repository.save(reservation);
        return toDto(savedReservation);
    }

    @Override
    public List<ReservationResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponseDto findById(Long idReservation) {
        Reservation reservation = repository.findById(idReservation).orElse(null);
        if (reservation == null) {
            throw new RuntimeException("servera de " + idReservation + " no encontrado en el sistema.");
        }
        return toDto(reservation);
    }

    @Override
    public ReservationResponseDto updateStatus(Long idReservation, String status) {
        Reservation reservation = repository.findById(idReservation).orElse(null);
        if (reservation == null) {
            throw new RuntimeException("la reserva " + idReservation + " no encontrado.");
        }
        reservation.setStatus(status.toUpperCase());
        Reservation updatedReservation = repository.save(reservation);
        return toDto(updatedReservation);
    }
}
