package cl.duocuc.review.client;


import cl.duocuc.review.dto.BookResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service",url = "http://localhost:8081/api/v1/books")
public interface BookClient {

    @GetMapping("/{isbn}")
    BookResponseDto getBookByIsbn(@PathVariable("isbn") Long isbn);
}
