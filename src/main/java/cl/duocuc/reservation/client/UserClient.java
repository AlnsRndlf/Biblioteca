package cl.duocuc.reservation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-client", url = "http://localhost:8081/api/books")
public interface UserClient {

    @GetMapping("{rut}")
    UserResponseDto getUserByRut(@PathVariable("rut") String rut);
}
