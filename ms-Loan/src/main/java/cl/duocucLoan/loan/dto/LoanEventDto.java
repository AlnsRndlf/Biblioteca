package cl.duocucLoan.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanEventDto {
    private Long loanId;
    private String userRut;
    private Long bookIsbn;
    private LocalDate loanDate;
    private String userEmail;
}
