package cl.duocucLoan.loan.service;

import cl.duocucLoan.loan.client.BookClient;
import cl.duocucLoan.loan.client.UserClient;
import cl.duocucLoan.loan.dto.*;
import cl.duocucLoan.loan.model.Loan;
import cl.duocucLoan.loan.repository.ILoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
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
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<LoanResponseDto> findByUserRut(String rut) {
        try {
            userClient.getUserByRut(rut);
        } catch (feign.FeignException.NotFound e) {
            throw new IllegalArgumentException("El usuario " + rut + " no existe.");
        }
        return repository.findByUserRut(rut)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public LoanResponseDto findById(Long idLoan) {
        Loan loan = repository.findById(idLoan).orElse(null);
        if  (loan == null) {
            throw new RuntimeException("prestamo de " +  idLoan + " no encontrado");
        }
        return toDto(loan);
    }

    @Override
    public LoanResponseDto returnBook(Long idLoan) {
        Loan loan = repository.findById(idLoan).orElse(null);
        if (loan == null) {
            throw new RuntimeException("prestamo de " +  idLoan + " no encontrado");
        }
        if  (loan.getReturnedDate() != null) { //pq ya hay una
            throw new RuntimeException("libro marcado como devuelto"); // fecha en el entity
        }
        loan.setReturnedDate(LocalDate.now());
        bookClient.updateStock(loan.getBookIsbn(),1);
        Loan updatedLoan = repository.save(loan);
        return toDto(updatedLoan);
    }

    @Override
    public LoanResponseDto save(LoanRequestDto request) {
        UserResponseDto userDto = userClient.getUserByRut(request.getUserRut());
        if (userDto == null) {
            throw new RuntimeException("usuario no encontrado.");
        }
        BookResponseDto bookDto = bookClient.getBookByIsbn(request.getBookIsbn());
        if (bookDto == null) {
            throw new RuntimeException("libro no encontrado.");
        }
        Loan loan = toEntity(request);
        bookClient.updateStock(request.getBookIsbn(), -1);
        Loan savedLoan = repository.save(loan);
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
        return toDto(savedLoan);
    }

}
