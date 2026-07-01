package cl.duocucLoan.loan.service;

import cl.duocucLoan.loan.client.BookClient;
import cl.duocucLoan.loan.client.UserClient;
import cl.duocucLoan.loan.dto.LoanRequestDto;
import cl.duocucLoan.loan.dto.LoanResponseDto;
import cl.duocucLoan.loan.dto.external.BookResponseDto;
import cl.duocucLoan.loan.dto.external.UserResponseDto;
import cl.duocucLoan.loan.model.Loan;
import cl.duocucLoan.loan.repository.ILoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private ILoanRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private BookClient bookClient;

    @InjectMocks
    private LoanServiceImpl service;

    private LoanRequestDto requestDto;
    private UserResponseDto userDto;
    private BookResponseDto bookDto;
    private Loan loanEntity;

    @BeforeEach
    void setUp() {
        requestDto = new LoanRequestDto();
        requestDto.setUserRut("11111111-1");
        requestDto.setBookIsbn(9781234567890L);

        userDto = new UserResponseDto();
        userDto.setUserRut("11111111-1");
        userDto.setUserName("Juan Perez");

        bookDto = new BookResponseDto();
        bookDto.setBookIsbn(9781234567890L);
        bookDto.setBookTitle("Libro de Prueba");
        bookDto.setStock(5); // Le asignamos stock positivo

        loanEntity = new Loan();
        loanEntity.setIdLoan(1L);
        loanEntity.setUserRut("11111111-1");
        loanEntity.setBookIsbn(9781234567890L);
        loanEntity.setLoanDate(LocalDate.now());
    }

    @Test
    void save_Success() {
        // Arrange
        when(userClient.getUserByRut(anyString())).thenReturn(userDto);
        when(bookClient.getBookByIsbn(anyLong())).thenReturn(bookDto);
        when(bookClient.updateStock(anyLong(), anyInt())).thenReturn(bookDto);
        when(repository.save(any(Loan.class))).thenReturn(loanEntity);

        // Act
        LoanResponseDto result = service.save(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdLoan());
        assertEquals("11111111-1", result.getUser().getUserRut());

        verify(bookClient, times(1)).updateStock(9781234567890L, -1);
        verify(repository, times(1)).save(any(Loan.class));
    }

    @Test
    void save_BookOutOfStock_ThrowsException() {
        // Arrange: Simulamos que el cliente nos devuelve el libro pero con stock 0
        bookDto.setStock(0);
        when(userClient.getUserByRut(anyString())).thenReturn(userDto);
        when(bookClient.getBookByIsbn(anyLong())).thenReturn(bookDto);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.save(requestDto);
        });

        assertTrue(exception.getMessage().contains("stock disponible"));

        // Verificamos que NUNCA intentó descontar stock ni guardar el préstamo
        verify(bookClient, never()).updateStock(anyLong(), anyInt());
        verify(repository, never()).save(any(Loan.class));
    }

    @Test
    void returnBook_Success() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(loanEntity));
        when(bookClient.updateStock(anyLong(), anyInt())).thenReturn(bookDto);
        when(repository.save(any(Loan.class))).thenReturn(loanEntity);
        // Cuando el método toDto internamente consulte a user y book, tenemos que simularlo:
        when(userClient.getUserByRut(anyString())).thenReturn(userDto);
        when(bookClient.getBookByIsbn(anyLong())).thenReturn(bookDto);

        // Act
        LoanResponseDto result = service.returnBook(1L);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getReturnedDate());

        verify(bookClient, times(1)).updateStock(9781234567890L, 1);
        verify(repository, times(1)).save(any(Loan.class));
    }
}