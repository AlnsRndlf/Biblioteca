package cl.duocucLoan.loan.client;


import cl.duocucLoan.loan.dto.BookResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "boook-service", url = "http://localhost:8081/api/v1/libros")
public interface BookClient {

    @GetMapping("/{isbn}")
    BookResponseDto getBookByIsbn(@PathVariable("isbn") Long isbn);

    @PutMapping("/{isbn}/stock/{quantity}")
    BookResponseDto updateStock(@PathVariable("isbn") Long isbn, @PathVariable("quantity") int quantity);
}
