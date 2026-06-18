package cl.duocucLoan.loan.controller;

import cl.duocucLoan.loan.dto.ExceptionDto;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExcpetionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleException(RuntimeException ex) {
        ExceptionDto error = new ExceptionDto(
                "error de  negocio",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleException(Exception ex) {
        ExceptionDto error = new ExceptionDto(
                "error interno del servidor",
                ex.getMessage()
                //request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExceptionDto> handleFeignException(FeignException ex) {
        String message = "el recurso no existe";
        String url = ex.request().url();
        if(url.contains("users")) {
            message = "usuario no encontrado";
        }
        if (url.contains("books")) {
            message = "libro no encontrado";
        }
        return new ResponseEntity<>(new ExceptionDto("error de negocio", message), HttpStatus.NOT_FOUND);
    }
}
