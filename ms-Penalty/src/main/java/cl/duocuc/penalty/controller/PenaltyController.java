package cl.duocuc.penalty.controller;

import cl.duocuc.penalty.dto.PenaltyRequestDto;
import cl.duocuc.penalty.dto.PenaltyResponseDto;
import cl.duocuc.penalty.service.IPenaltyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/multas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Multas", description = "Endpoints para el registro y consulta de multas a usuarios")
public class PenaltyController {

    private final IPenaltyService service;

    @Operation(summary = "Obtener todas las multas", description = "Retorna el registro histórico completo de todas las multas cursadas.")
    @GetMapping
    public ResponseEntity<List<PenaltyResponseDto>> getAll() {
        log.info("Solicitud recibida para obtener el registro histórico de todas las multas.");
        List<PenaltyResponseDto> responses = service.findAll();
        log.info("Se retornaron {} multas en total.", responses.size());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Buscar multas por RUT", description = "Busca todas las multas (pendientes y pagadas) asociadas a un usuario específico.")
    @GetMapping("/usuario/{rut}")
    public ResponseEntity<List<PenaltyResponseDto>> getByRut(
            @Parameter(description = "RUT del usuario infractor", required = true) @PathVariable("rut") String rut) {
        log.info("Solicitud recibida para buscar el historial de multas del usuario RUT: {}", rut);
        List<PenaltyResponseDto> responses = service.findByRut(rut);
        log.info("Se encontraron {} multas asociadas al RUT: {}", responses.size(), rut);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "Cursar nueva multa", description = "Genera una nueva multa validando previamente que el usuario exista en el sistema.")
    @ApiResponse(responseCode = "201", description = "Multa creada exitosamente con estado 'pendiente'")
    @PostMapping
    public ResponseEntity<PenaltyResponseDto> save(@RequestBody PenaltyRequestDto request) {
        log.info("Solicitud recibida para registrar una nueva multa. RUT: {}, Monto: {}, Motivo: '{}'",
                request.getUserRut(), request.getAmount(), request.getReason());
        PenaltyResponseDto response = service.save(request);
        log.info("Multa registrada exitosamente con ID: {}", response.getIdPenalty());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}