package cl.duocuc.penalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PenaltyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PenaltyApplication.class, args);
	}

}
