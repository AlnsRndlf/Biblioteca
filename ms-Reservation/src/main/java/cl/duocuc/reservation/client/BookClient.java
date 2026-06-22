package cl.duocuc.reservation.client;

import cl.duocuc.reservation.dto.external.BookResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/v1/books")
public interface BookClient {

    @GetMapping("/{isbn}")
    BookResponseDto getBookByIsbn(@PathVariable("isbn") Long isbn);
}
