package cl.duocuc.reservation.client;

import cl.duocuc.reservation.dto.external.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-client", url = "http://localhost:8082/api/v1/usuarios")
public interface UserClient {

    @GetMapping("{rut}")
    UserResponseDto getUserByRut(@PathVariable("rut") String rut);
}
