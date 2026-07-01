package cl.duocuc.reservation.service;

import cl.duocuc.reservation.client.BookClient;
import cl.duocuc.reservation.client.UserClient;
import cl.duocuc.reservation.dto.ReservationRequestDto;
import cl.duocuc.reservation.dto.ReservationResponseDto;
import cl.duocuc.reservation.dto.external.BookResponseDto;
import cl.duocuc.reservation.dto.external.UserResponseDto;
import cl.duocuc.reservation.model.Reservation;
import cl.duocuc.reservation.repository.ReservationRepository;
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
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private BookClient bookClient;

    @InjectMocks
    private ReservationServiceImpl service;

    private ReservationRequestDto requestDto;
    private UserResponseDto userDto;
    private BookResponseDto bookDto;
    private Reservation reservationEntity;

    @BeforeEach
    void setUp() {
        requestDto = new ReservationRequestDto();
        requestDto.setUserRut("11111111-1");
        requestDto.setBookIsbn(9781234567890L);

        userDto = new UserResponseDto();
        userDto.setUserRut("11111111-1");

        bookDto = new BookResponseDto();
        bookDto.setBookIsbn(9781234567890L);
        // para q sea reserva tienen q haber 0 ejemplares (reservar 1 para dsp xd) sino seria un prestamo.
        bookDto.setStock(0);

        reservationEntity = new Reservation();
        reservationEntity.setIdReservation(1L);
        reservationEntity.setUserRut("11111111-1");
        reservationEntity.setBookIsbn(9781234567890L);
        reservationEntity.setReservationDate(LocalDate.now());
        reservationEntity.setStatus("PENDIENTE");
    }

    @Test
    void save_Success_WhenBookHasNoStock() {
        // Arrange
        when(userClient.getUserByRut(anyString())).thenReturn(userDto);
        when(bookClient.getBookByIsbn(anyLong())).thenReturn(bookDto);
        when(repository.save(any(Reservation.class))).thenReturn(reservationEntity);

        // Act
        ReservationResponseDto result = service.save(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdReservation());
        assertEquals("PENDIENTE", result.getStatus());
        verify(repository, times(1)).save(any(Reservation.class));
    }

    @Test
    void save_Fails_WhenBookHasStock() {
        // Arrange: Le decimos al Mock que el libro SÍ tiene stock (5 unidades)
        bookDto.setStock(5);
        when(userClient.getUserByRut(anyString())).thenReturn(userDto);
        when(bookClient.getBookByIsbn(anyLong())).thenReturn(bookDto);

        // Act & Assert: Debe estallar una excepción impidiendo la reserva
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.save(requestDto);
        });

        // Verificamos que el mensaje de error es por culpa del stock
        assertTrue(exception.getMessage().toLowerCase().contains("stock"));

        // Verificamos que el sistema se protegió y NUNCA lo guardó en la base de datos
        verify(repository, never()).save(any(Reservation.class));
    }

    @Test
    void updateStatus_Success() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(reservationEntity));
        when(repository.save(any(Reservation.class))).thenReturn(reservationEntity);

        // Act: Cambiamos el estado a COMPLETADA
        ReservationResponseDto result = service.updateStatus(1L, "COMPLETADA");

        // Assert
        assertNotNull(result);
        verify(repository, times(1)).save(any(Reservation.class));
    }

    @Test
    void findById_NotFound_ThrowsException() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.findById(99L);
        });

        assertTrue(exception.getMessage().contains("no encontrada"));
    }
}