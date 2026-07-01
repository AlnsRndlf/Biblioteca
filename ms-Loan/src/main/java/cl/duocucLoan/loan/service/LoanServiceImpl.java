package cl.duocucLoan.loan.service;

import cl.duocucLoan.loan.client.BookClient;
import cl.duocucLoan.loan.client.UserClient;
import cl.duocucLoan.loan.dto.*;
import cl.duocucLoan.loan.dto.external.BookResponseDto;
import cl.duocucLoan.loan.dto.external.UserResponseDto;
import cl.duocucLoan.loan.model.Loan;
import cl.duocucLoan.loan.repository.ILoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements ILoanService {

    private final ILoanRepository repository;
    private final UserClient userClient;
    private final BookClient bookClient;
    //private final KafkaProducer producer;

    private LoanResponseDto toDto(Loan loan) {
        UserResponseDto userDto = userClient.getUserByRut(loan.getUserRut());
        BookResponseDto bookDto = bookClient.getBookByIsbn(loan.getBookIsbn());
        return new LoanResponseDto(
                loan.getIdLoan(),
                bookDto,
                userDto,
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnedDate()
        );
    }

    private Loan toEntity(LoanRequestDto request) {
        Loan loan = new Loan();
        loan.setUserRut(request.getUserRut());
        loan.setBookIsbn(request.getBookIsbn());
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setReturnedDate(null); // se usa null pq no se sabe cuando lo van a devolver
        return loan;
    }

    @Override
    public List<LoanResponseDto> findAll() {
        log.info("Consultando la base de datos para obtener todos los préstamos.");
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<LoanResponseDto> findByUserRut(String rut) {
        log.info("Iniciando búsqueda de préstamos para el RUT: {}", rut);
        try {
            log.info("Verificando existencia del usuario mediante UserClient...");
            userClient.getUserByRut(rut);
        } catch (feign.FeignException.NotFound e) {
            log.warn("Búsqueda fallida: El usuario RUT {} no existe en ms-User.", rut);
            throw new IllegalArgumentException("El usuario " + rut + " no existe.");
        }

        log.info("Usuario validado. Obteniendo historial de préstamos de la BD.");
        return repository.findByUserRut(rut)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public LoanResponseDto findById(Long idLoan) {
        log.info("Buscando en BD el préstamo con ID: {}", idLoan);
        Loan loan = repository.findById(idLoan).orElse(null);
        if  (loan == null) {
            log.warn("Búsqueda fallida: Préstamo ID {} no encontrado", idLoan);
            throw new RuntimeException("prestamo de " +  idLoan + " no encontrado");
        }
        log.info("Préstamo ID {} encontrado con éxito.", idLoan);
        return toDto(loan);
    }

    @Override
    public LoanResponseDto returnBook(Long idLoan) {
        log.info("Iniciando proceso de devolución para el préstamo ID: {}", idLoan);
        Loan loan = repository.findById(idLoan).orElse(null);

        if (loan == null) {
            log.warn("Devolución fallida: El préstamo ID {} no existe.", idLoan);
            throw new RuntimeException("prestamo de " +  idLoan + " no encontrado");
        }
        if  (loan.getReturnedDate() != null) { //pq ya hay una
            log.warn("Devolución rechazada: El préstamo ID {} ya se encontraba devuelto desde {}", idLoan, loan.getReturnedDate());
            throw new RuntimeException("libro marcado como devuelto"); // fecha en el entity
        }

        loan.setReturnedDate(LocalDate.now());

        log.info("Restaurando stock (+1) para el libro ISBN {} mediante BookClient...", loan.getBookIsbn());
        bookClient.updateStock(loan.getBookIsbn(), 1);

        Loan updatedLoan = repository.save(loan);
        log.info("Préstamo ID {} marcado como devuelto en la base de datos exitosamente.", idLoan);
        return toDto(updatedLoan);
    }

    @Override
    public LoanResponseDto save(LoanRequestDto request) {
        log.info("Iniciando validaciones para autorizar préstamo. RUT: {}, Libro ISBN: {}", request.getUserRut(), request.getBookIsbn());

        log.info("Validando datos del usuario en ms-User...");
        UserResponseDto userDto = userClient.getUserByRut(request.getUserRut());
        if (userDto == null) {
            log.error("Creación fallida: Usuario RUT {} no encontrado.", request.getUserRut());
            throw new RuntimeException("usuario no encontrado.");
        }

        log.info("Validando disponibilidad del libro en ms-Book...");
        BookResponseDto bookDto = bookClient.getBookByIsbn(request.getBookIsbn());
        if (bookDto == null) {
            log.error("Creación fallida: Libro ISBN {} no encontrado.", request.getBookIsbn());
            throw new RuntimeException("libro no encontrado.");
        }

        if (bookDto.getStock() == null || bookDto.getStock() <= 0) {
            log.error("Creación fallida: Libro ISBN {} no tiene stock disponible (Stock actual: {}).", request.getBookIsbn(), bookDto.getStock());
            throw new RuntimeException("El libro no tiene stock disponible para préstamo.");
        }

        Loan loan = toEntity(request);

        log.info("Descontando stock (-1) del libro ISBN {} mediante BookClient...", request.getBookIsbn());
        bookClient.updateStock(request.getBookIsbn(), -1);

        Loan savedLoan = repository.save(loan);
        log.info("Préstamo registrado exitosamente en BD con ID: {}", savedLoan.getIdLoan());

        /*
        LoanEventDto event = new LoanEventDto(
                savedLoan.getIdLoan(),
                savedLoan.getUserRut(),
                savedLoan.getBookIsbn(),
                savedLoan.getLoanDate(),
                userDto.getUserEmail()
        );
        */
        //producer.sendLoandEvent(event); lo vimo kafka. inconfigurable el qlo
        log.info("Proceso de guardado finalizado. Retornando DTO de respuesta.");
        return toDto(savedLoan);
    }

}