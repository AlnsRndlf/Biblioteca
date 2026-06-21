package cl.duocucLoan.loan.service;

import cl.duocucLoan.loan.dto.LoanRequestDto;
import cl.duocucLoan.loan.dto.LoanResponseDto;

import java.util.List;

public interface ILoanService {

    List<LoanResponseDto> findAll();
    List<LoanResponseDto> findByUserRut(String rut);
    LoanResponseDto findById(Long idLoan);
    LoanResponseDto returnBook(Long idLoan);

    //recibe un rut/isbn request (de tipo request pq ahi ta rut/isbn) y devuelve el user/book entero (response)
    LoanResponseDto save(LoanRequestDto request);

}
