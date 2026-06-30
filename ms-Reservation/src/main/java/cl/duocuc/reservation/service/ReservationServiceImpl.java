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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("Iniciando validaciones para autorizar reserva. RUT: {}, ISBN: {}", request.getUserRut(), request.getBookIsbn());

        log.info("Consultando existencia del usuario mediante UserClient...");
        UserResponseDto userDto = userClient.getUserByRut(request.getUserRut());
        if (userDto == null) {
            log.warn("Reserva rechazada: El usuario RUT {} no existe en ms-User.", request.getUserRut());
            throw new RuntimeException("Usuario no encontrado en el sistema.");
        }

        log.info("Consultando existencia y disponibilidad del libro mediante BookClient...");
        BookResponseDto bookDto = bookClient.getBookByIsbn(request.getBookIsbn());
        if (bookDto == null) {
            log.warn("Reserva rechazada: El libro ISBN {} no existe en ms-Book.", request.getBookIsbn());
            throw new RuntimeException("Libro no encontrado en el sistema.");
        }
        if (bookDto.getStock() > 0) {
            log.warn("Reserva rechazada: El libro ISBN {} tiene stock positivo ({}). No se requiere reserva, se puede pedir prestado directamente.", request.getBookIsbn(), bookDto.getStock());
            throw new RuntimeException("Hay disponibles para retirar.");
        }

        log.info("Validaciones superadas. El libro no tiene stock, procediendo a guardar la reserva...");
        Reservation reservation = toEntity(request);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("pendiente");

        Reservation savedReservation = repository.save(reservation);
        log.info("Reserva guardada exitosamente en la base de datos con ID: {}", savedReservation.getIdReservation());
        return toDto(savedReservation);
    }

    @Override
    public List<ReservationResponseDto> findAll() {
        log.info("Consultando la base de datos para recuperar todas las reservas.");
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponseDto findById(Long idReservation) {
        log.info("Buscando en BD la reserva con ID: {}", idReservation);
        Reservation reservation = repository.findById(idReservation).orElse(null);
        if (reservation == null) {
            log.warn("Búsqueda fallida: La reserva ID {} no existe.", idReservation);
            throw new RuntimeException("La reserva de " + idReservation + " no encontrada en el sistema.");
        }
        log.info("Reserva ID {} encontrada exitosamente.", idReservation);
        return toDto(reservation);
    }

    @Override
    public ReservationResponseDto updateStatus(Long idReservation, String status) {
        log.info("Iniciando actualización de estado para la reserva ID: {}. Nuevo estado: '{}'", idReservation, status);
        Reservation reservation = repository.findById(idReservation).orElse(null);

        if (reservation == null) {
            log.warn("Actualización fallida: La reserva ID {} no existe.", idReservation);
            throw new RuntimeException("La reserva " + idReservation + " no encontrada.");
        }

        reservation.setStatus(status.toUpperCase());
        Reservation updatedReservation = repository.save(reservation);
        log.info("Estado de la reserva ID {} actualizado a '{}' en la base de datos.", idReservation, updatedReservation.getStatus());

        return toDto(updatedReservation);
    }
}