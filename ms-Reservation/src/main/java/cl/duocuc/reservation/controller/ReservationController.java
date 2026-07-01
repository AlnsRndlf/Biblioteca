package cl.duocuc.reservation.controller;

import cl.duocuc.reservation.dto.ReservationRequestDto;
import cl.duocuc.reservation.dto.ReservationResponseDto;
import cl.duocuc.reservation.service.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reservas", description = "Endpoints para la gestión de reservas de libros que no tienen stock disponible")
public class ReservationController {

    final IReservationService service;

    @Operation(summary = "Crear una reserva", description = "Genera una reserva para un libro sin stock. Falla si el libro aún tiene stock disponible.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "El libro tiene stock (debe pedir préstamo) o usuario inválido")
    })
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        log.info("Solicitud recibida para crear una reserva. Usuario RUT: {}, Libro ISBN: {}", requestDto.getUserRut(), requestDto.getBookIsbn());
        ReservationResponseDto response = service.save(requestDto);
        log.info("Reserva creada exitosamente respondiendo al cliente. ID Reserva: {}", response.getIdReservation());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todas las reservas", description = "Retorna el historial completo de reservas.")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        log.info("Solicitud recibida para obtener todas las reservas históricas.");
        List<ReservationResponseDto> response = service.findAll();
        log.info("Se retornaron {} reservas en total.", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar reserva por ID", description = "Obtiene los detalles de una reserva en particular.")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(
            @Parameter(description = "ID de la reserva", required = true) @PathVariable("id") Long id) {
        log.info("Solicitud recibida para buscar los detalles de la reserva ID: {}", id);
        ReservationResponseDto response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar estado de reserva", description = "Modifica el estado de una reserva (ej: PENDIENTE, COMPLETADA, CANCELADA).")
    @PatchMapping("/{id}/{status}")
    public ResponseEntity<ReservationResponseDto> updateReservationStatus(
            @Parameter(description = "ID de la reserva", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Nuevo estado a asignar", required = true) @PathVariable("status") String status) {
        log.info("Solicitud recibida para actualizar el estado de la reserva ID: {} a '{}'", id, status);
        ReservationResponseDto response = service.updateStatus(id, status);
        log.info("Estado de la reserva ID {} actualizado exitosamente a '{}'", id, status);
        return ResponseEntity.ok(response);
    }
}