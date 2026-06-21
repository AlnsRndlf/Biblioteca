package cl.duocucLoan.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ExceptionDto {

    private String message;
    private String error;
    //private String path;
}
