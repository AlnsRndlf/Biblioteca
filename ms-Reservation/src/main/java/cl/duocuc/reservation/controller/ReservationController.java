package cl.duocuc.reservation.controller;

import cl.duocuc.reservation.dto.ReservationRequestDto;
import cl.duocuc.reservation.dto.ReservationResponseDto;
import cl.duocuc.reservation.service.IReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    final IReservationService service;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        log.info("Solicitud recibida para crear una reserva. Usuario RUT: {}, Libro ISBN: {}", requestDto.getUserRut(), requestDto.getBookIsbn());
        ReservationResponseDto response = service.save(requestDto);
        log.info("Reserva creada exitosamente respondiendo al cliente. ID Reserva: {}", response.getIdReservation());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        log.info("Solicitud recibida para obtener todas las reservas históricas.");
        List<ReservationResponseDto> response = service.findAll();
        log.info("Se retornaron {} reservas en total.", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable("id") Long id) {
        log.info("Solicitud recibida para buscar los detalles de la reserva ID: {}", id);
        ReservationResponseDto response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<ReservationResponseDto> updateReservationStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") String status) {
        log.info("Solicitud recibida para actualizar el estado de la reserva ID: {} a '{}'", id, status);
        ReservationResponseDto response = service.updateStatus(id, status);
        log.info("Estado de la reserva ID {} actualizado exitosamente a '{}'", id, status);
        return ResponseEntity.ok(response);
    }
}