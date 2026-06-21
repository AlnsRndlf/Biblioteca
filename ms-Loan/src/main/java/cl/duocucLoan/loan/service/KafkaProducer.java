package cl.duocucLoan.loan.service;


import cl.duocucLoan.loan.dto.LoanEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, LoanEventDto> kafkaTemplate;
    private static final String TOPIC_LOAN_EVENT = "evento-prestamos";
    public void sendLoandEvent(LoanEventDto event) {
        log.info("enviando el evento del prestamo  por el libro de ISBN{}", event.getBookIsbn());
        this.kafkaTemplate.send(TOPIC_LOAN_EVENT, event);
    }
}
