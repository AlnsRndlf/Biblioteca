package cl.duocucLoan.loan.controller;

import cl.duocucLoan.loan.dto.LoanRequestDto;
import cl.duocucLoan.loan.dto.LoanResponseDto;
import cl.duocucLoan.loan.service.ILoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Préstamos", description = "Endpoints para la gestión de préstamos y devoluciones de libros")
public class LoanController  {

    private final ILoanService service;

    @Operation(summary = "Obtener historial completo", description = "Retorna una lista con todos los préstamos registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<LoanResponseDto>> findAll() {
        log.info("Solicitud recibida para obtener el historial completo de préstamos.");
        List<LoanResponseDto> loans = service.findAll();
        log.info("Se retornaron {} préstamos encontrados.", loans.size());
        return ResponseEntity.ok(loans);
    }

    @Operation(summary = "Buscar préstamo por ID", description = "Busca los detalles de un préstamo específico utilizando su identificador único.")
    @GetMapping("/{idLoan}")
    public ResponseEntity<LoanResponseDto> findByIdLoan(
            @Parameter(description = "ID único del préstamo", required = true) @PathVariable("idLoan") Long idLoan) {
        log.info("Solicitud recibida para buscar detalles del préstamo ID: {}", idLoan);
        return ResponseEntity.ok(service.findById(idLoan));
    }

    @Operation(summary = "Buscar préstamos por RUT", description = "Obtiene todo el historial de préstamos asociados a un usuario específico.")
    @GetMapping("/usuario/{rut}")
    public ResponseEntity<List<LoanResponseDto>> findByUser(
            @Parameter(description = "RUT del usuario (Ej: 11111111-1)", required = true) @PathVariable("rut") String rut) {
        log.info("Solicitud recibida para buscar el historial de préstamos del usuario RUT: {}", rut);
        List<LoanResponseDto> userLoans = service.findByUserRut(rut);
        log.info("Se encontraron {} préstamos para el usuario RUT: {}", userLoans.size(), rut);
        return ResponseEntity.ok(userLoans);
    }

    @Operation(summary = "Registrar un nuevo préstamo", description = "Crea un nuevo préstamo verificando la existencia del usuario y la disponibilidad de stock del libro.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Préstamo creado y stock descontado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Usuario no existe o Libro sin stock")
    })
    @PostMapping
    public ResponseEntity<LoanResponseDto> save(@RequestBody LoanRequestDto requestDto) {
        log.info("Solicitud recibida para registrar un nuevo préstamo. RUT: {}, Libro ISBN: {}", requestDto.getUserRut(), requestDto.getBookIsbn());
        LoanResponseDto savedLoan = service.save(requestDto);
        log.info("Préstamo registrado exitosamente y respondiendo al cliente. ID Préstamo: {}", savedLoan.getIdLoan());
        return new ResponseEntity<>(savedLoan, HttpStatus.CREATED);
    }

    @Operation(summary = "Devolver un libro", description = "Marca un préstamo como devuelto y restaura (+1) el stock del libro en el inventario.")
    @PatchMapping("/{idLoan}/return")
    public ResponseEntity<LoanResponseDto> returnBook(
            @Parameter(description = "ID del préstamo a devolver", required = true) @PathVariable("idLoan") Long idLoan) {
        log.info("Solicitud recibida para registrar la devolución del préstamo ID: {}", idLoan);
        LoanResponseDto returnedLoan = service.returnBook(idLoan);
        log.info("Devolución procesada exitosamente para el préstamo ID: {}", idLoan);
        return new ResponseEntity<>(returnedLoan, HttpStatus.OK);
    }
}