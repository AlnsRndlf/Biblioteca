package cl.duocucLoan.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponseDto {

    private Long idLoan;

    private BookResponseDto book;
    private UserResponseDto user;

    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;

}
