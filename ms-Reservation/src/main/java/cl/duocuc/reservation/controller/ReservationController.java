package cl.duocuc.reservation.controller;

import cl.duocuc.reservation.dto.ReservationRequestDto;
import cl.duocuc.reservation.dto.ReservationResponseDto;
import cl.duocuc.reservation.service.IReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservationController {

    final IReservationService service;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto response = service.save(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable("id") Long id) {
        ReservationResponseDto response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<ReservationResponseDto> updateReservationStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") String status) {
        ReservationResponseDto response = service.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }
}
