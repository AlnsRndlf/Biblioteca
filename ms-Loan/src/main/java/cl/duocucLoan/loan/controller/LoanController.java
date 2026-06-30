package cl.duocucLoan.loan.controller;

import cl.duocucLoan.loan.dto.LoanRequestDto;
import cl.duocucLoan.loan.dto.LoanResponseDto;
import cl.duocucLoan.loan.service.ILoanService;
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
public class LoanController  {

    private final ILoanService service;

    @GetMapping
    public ResponseEntity<List<LoanResponseDto>> findAll() {
        log.info("Solicitud recibida para obtener el historial completo de préstamos.");
        List<LoanResponseDto> loans = service.findAll();
        log.info("Se retornaron {} préstamos encontrados.", loans.size());
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{idLoan}")
    public ResponseEntity<LoanResponseDto> findByIdLoan(@PathVariable("idLoan") Long idLoan) {
        log.info("Solicitud recibida para buscar detalles del préstamo ID: {}", idLoan);
        return ResponseEntity.ok(service.findById(idLoan));
    }

    @GetMapping("/usuario/{rut}")
    public ResponseEntity<List<LoanResponseDto>> findByUser(@PathVariable("rut") String rut) {
        log.info("Solicitud recibida para buscar el historial de préstamos del usuario RUT: {}", rut);
        List<LoanResponseDto> userLoans = service.findByUserRut(rut);
        log.info("Se encontraron {} préstamos para el usuario RUT: {}", userLoans.size(), rut);
        return ResponseEntity.ok(userLoans);
    }

    @PostMapping
    public ResponseEntity<LoanResponseDto> save(@RequestBody LoanRequestDto requestDto) {
        log.info("Solicitud recibida para registrar un nuevo préstamo. RUT: {}, Libro ISBN: {}", requestDto.getUserRut(), requestDto.getBookIsbn());
        LoanResponseDto savedLoan = service.save(requestDto);
        log.info("Préstamo registrado exitosamente y respondiendo al cliente. ID Préstamo: {}", savedLoan.getIdLoan());
        return new ResponseEntity<>(savedLoan, HttpStatus.CREATED);
    }

    @PatchMapping("/{idLoan}/return")
    public ResponseEntity<LoanResponseDto> returnBook(@PathVariable("idLoan") Long idLoan) {
        log.info("Solicitud recibida para registrar la devolución del préstamo ID: {}", idLoan);
        LoanResponseDto returnedLoan = service.returnBook(idLoan);
        log.info("Devolución procesada exitosamente para el préstamo ID: {}", idLoan);
        return new ResponseEntity<>(returnedLoan, HttpStatus.OK);
    }
}