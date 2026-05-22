package cl.duocucLoan.loan.client;

import cl.duocucLoan.loan.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="user-service", url = "http://localhost:8082/api/v1/users")
public interface UserClient {

    @GetMapping("/{rut}")
    UserResponseDto getUserByRut(@PathVariable("rut") String rut);

    // para sincronizar con kafka y java the rial mail sender ¿será?
    @GetMapping("/{email}")
    UserResponseDto getUserByEmail(@PathVariable("email") String email);
}
