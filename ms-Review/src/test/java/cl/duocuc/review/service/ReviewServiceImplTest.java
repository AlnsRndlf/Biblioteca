package cl.duocuc.review.service;

import cl.duocuc.review.client.BookClient;
import cl.duocuc.review.client.LoanClient;
import cl.duocuc.review.client.UserClient;
import cl.duocuc.review.dto.ReviewRequestDto;
import cl.duocuc.review.dto.ReviewResponseDto;
import cl.duocuc.review.dto.external.BookResponseDto;
import cl.duocuc.review.dto.external.LoanResponseDto;
import cl.duocuc.review.dto.external.UserResponseDto;
import cl.duocuc.review.model.Review;
import cl.duocuc.review.repository.IReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private IReviewRepository repository;

    @Mock
    private LoanClient loanClient;

    @Mock
    private UserClient userClient;

    @Mock
    private BookClient bookClient;

    // ¡TÉCNICA AVANZADA! Mockeamos los DTOs externos para evitar errores de setters de Lombok
    @Mock
    private UserResponseDto userDtoMock;

    @Mock
    private BookResponseDto bookDtoMock;

    @Mock
    private LoanResponseDto loanDtoMock;

    @InjectMocks
    private ReviewServiceImpl service;

    private ReviewRequestDto requestDto;
    private Review reviewEntity;

    @BeforeEach
    void setUp() {
        // Los objetos internos sí los podemos instanciar normalmente
        requestDto = new ReviewRequestDto();
        requestDto.setUserRut("11111111-1");
        requestDto.setBookIsbn(9781234567890L);
        requestDto.setRating(5);
        requestDto.setComment("¡Excelente libro!");

        reviewEntity = new Review();
        reviewEntity.setIdReview(1L);
        reviewEntity.setUserRut("11111111-1");
        reviewEntity.setBookIsbn(9781234567890L);
        reviewEntity.setRating(5);
        reviewEntity.setComment("¡Excelente libro!");
        reviewEntity.setCreatedAt(LocalDate.now());
    }

    @Test
    void save_Success() {
        // Arrange
        // Usamos lenient() para que Mockito no sea tan estricto si no usamos algún dato
        lenient().when(userDtoMock.getUserRut()).thenReturn("11111111-1");
        lenient().when(bookDtoMock.getBookIsbn()).thenReturn(9781234567890L);

        // Simulamos que en el historial de préstamos SÍ está el libro que queremos reseñar
        lenient().when(loanDtoMock.getBookIsbn()).thenReturn(9781234567890L);

        when(userClient.getUserByRut(anyString())).thenReturn(userDtoMock);
        when(bookClient.getBookByIsbn(anyLong())).thenReturn(bookDtoMock);
        // Simulamos la respuesta de LoanClient con nuestro préstamo falso
        when(loanClient.getUserByRut(anyString())).thenReturn(List.of(loanDtoMock));

        when(repository.save(any(Review.class))).thenReturn(reviewEntity);

        // Act
        ReviewResponseDto result = service.save(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdReview());
        assertEquals(5, result.getRating());
        verify(repository, times(1)).save(any(Review.class));
    }

    @Test
    void save_Fails_WhenNoPriorLoan() {
        // Arrange: El usuario y el libro existen...
        when(userClient.getUserByRut(anyString())).thenReturn(userDtoMock);
        when(bookClient.getBookByIsbn(anyLong())).thenReturn(bookDtoMock);

        // PERO el historial de préstamos viene VACÍO
        when(loanClient.getUserByRut(anyString())).thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.save(requestDto);
        });

        // Verificamos que el sistema detuvo la reseña por falta de préstamo
        assertTrue(exception.getMessage().contains("prestamo"));
        verify(repository, never()).save(any(Review.class));
    }

    @Test
    void save_Fails_WhenUserNotFound() {
        // Arrange: ms-User responde que el usuario no existe (null)
        when(userClient.getUserByRut(anyString())).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.save(requestDto);
        });

        assertTrue(exception.getMessage().contains("usuario no existe"));
        verify(repository, never()).save(any(Review.class));
    }

    @Test
    void findById_Success() {
        // Arrange
        lenient().when(userDtoMock.getUserRut()).thenReturn("11111111-1");
        lenient().when(bookDtoMock.getBookIsbn()).thenReturn(9781234567890L);

        when(repository.findById(1L)).thenReturn(Optional.of(reviewEntity));
        // Al transformar a DTO (toDto), el sistema buscará los datos externos
        when(userClient.getUserByRut(anyString())).thenReturn(userDtoMock);
        when(bookClient.getBookByIsbn(anyLong())).thenReturn(bookDtoMock);

        // Act
        ReviewResponseDto result = service.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getRating());
    }
}