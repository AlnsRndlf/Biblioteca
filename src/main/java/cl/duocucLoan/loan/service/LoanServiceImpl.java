package cl.duocucLoan.loan.service;

import cl.duocucLoan.loan.client.BookClient;
import cl.duocucLoan.loan.client.UserClient;
import cl.duocucLoan.loan.dto.BookResponseDto;
import cl.duocucLoan.loan.dto.LoanRequestDto;
import cl.duocucLoan.loan.dto.LoanResponseDto;
import cl.duocucLoan.loan.dto.UserResponseDto;
import cl.duocucLoan.loan.model.Loan;
import cl.duocucLoan.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private final LoanRepository loanRepository;
    private final UserClient userClient;
    private final BookClient bookClient;

    private LoanResponseDto toDto(Loan loan) {
        UserResponseDto userDto = userClient.getUserByRut(loan.getUserRut());
        if (userDto == null) {
            throw new RuntimeException("usuario  de rut "+loan.getUserRut()+" no encontrado");
        }
        BookResponseDto bookDto = bookClient.getBookByIsbn(loan.getBookIsbn());
        if (bookDto == null) {
            throw new RuntimeException("libro de isbn"+loan.getBookIsbn()+" no encontrado");
        }
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
        return loanRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<LoanResponseDto> findByUserRut(String rut) {
        return loanRepository.findByUserRut(rut)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public LoanResponseDto findById(Long idLoan) {
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        if  (loan == null) {
            throw new RuntimeException("prestamo de " +  idLoan + " no encontrado");
        }
        return toDto(loan);
    }

    @Override
    public LoanResponseDto returnBook(Long idLoan) {
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        if (loan == null) {
            throw new RuntimeException("prestamo de " +  idLoan + " no encontrado");
        }
        if  (loan.getReturnedDate() != null) {
            throw new RuntimeException("libro marcado como devuelto"); //pq hay fecha en el entity
        }
        loan.setReturnedDate(LocalDate.now());
        bookClient.updateStock(loan.getBookIsbn(),1);
        Loan updatedLoan = loanRepository.save(loan);
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
        Loan savedLoan = loanRepository.save(loan);
        return toDto(savedLoan);

    }

}
