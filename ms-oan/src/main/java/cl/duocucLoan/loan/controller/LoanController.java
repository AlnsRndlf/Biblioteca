package cl.duocucLoan.loan.controller;

import cl.duocucLoan.loan.dto.LoanRequestDto;
import cl.duocucLoan.loan.dto.LoanResponseDto;
import cl.duocucLoan.loan.service.ILoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
@RequiredArgsConstructor
public class LoanController  {

    private final ILoanService service;

    @GetMapping
    public ResponseEntity<List<LoanResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idLoan}")
    public ResponseEntity<LoanResponseDto> findByIdLoan(@PathVariable("idLoan") Long idLoan) {
        return ResponseEntity.ok(service.findById(idLoan));
    }

    @GetMapping("/user/{rut}")
    public ResponseEntity<List<LoanResponseDto>> findByUser(@PathVariable("rut") String rut) {
        return ResponseEntity.ok(service.findByUserRut(rut));
    }

    @PostMapping
    public ResponseEntity<LoanResponseDto> save(@RequestBody LoanRequestDto requestDto) {
        return new ResponseEntity<>(service.save(requestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{idLoan}/return")
    public ResponseEntity<LoanResponseDto> returnBook(@PathVariable("idLoan") Long idLoan) {
        return new ResponseEntity<>(service.returnBook(idLoan), HttpStatus.OK);
    }
}
