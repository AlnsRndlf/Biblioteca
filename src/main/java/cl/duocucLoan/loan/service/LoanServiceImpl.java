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
        return loanRepository.findAll()
                .stream()
                .map(this::toDto).toList();
    }

    @Override
    public LoanResponseDto findById(Long idLoan) {
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        if  (loan == null) {
            throw new RuntimeException("prestamo de " +  idLoan + " no encontrado");
        }
        return toDto(loan);
    }



}
