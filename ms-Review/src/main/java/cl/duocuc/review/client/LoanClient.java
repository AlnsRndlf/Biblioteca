package cl.duocuc.review.client;


import cl.duocuc.review.dto.external.LoanResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="loan-service", url = "http://localhost:8083/api/v1/prestamos")
public interface LoanClient {

    @GetMapping("/user/{rut}")
    List<LoanResponseDto> getUserByRut(@PathVariable("rut") String rut);
}
