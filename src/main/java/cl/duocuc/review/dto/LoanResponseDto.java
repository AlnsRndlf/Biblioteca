package cl.duocuc.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponseDto {

    private Long idLoan;
    private String userRut;
    private String bookIsbn;
}
