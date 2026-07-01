/*
package cl.duocuc.notification.customer;

import cl.duocuc.notification.dto.LoanEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoanCustomer {

    @KafkaListener(topics = "prestamos-eventos", groupId = "notificaciones-group")
    public void listenLoanEvents(LoanEventDto event) {
        log.info("=========================================================");
        log.info("¡NUEVO EVENTO LLEGÓ DESDE KAFKA!");
        log.info("Procesando notificación para el Préstamo ID: {}", event.getLoanId());
        log.info("Enviando correo de confirmación a: {}", event.getUserEmail());
        log.info("Detalles del libro: ISBN {}", event.getBookIsbn());
        log.info("=========================================================");
        // depedencia java mail añadida para dsp poder mandar las notificaciones al correo del usuario
    }
}
*/