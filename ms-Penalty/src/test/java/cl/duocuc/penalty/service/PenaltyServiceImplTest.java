package cl.duocuc.penalty.service;

import cl.duocuc.penalty.client.UserClient;
import cl.duocuc.penalty.dto.PenaltyRequestDto;
import cl.duocuc.penalty.dto.PenaltyResponseDto;
import cl.duocuc.penalty.dto.external.UserResponseDto;
import cl.duocuc.penalty.model.Penalty;
import cl.duocuc.penalty.repository.IPenaltyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PenaltyServiceImplTest {

    @Mock
    private IPenaltyRepository repository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private PenaltyServiceImpl service;

    private PenaltyRequestDto requestDto;
    private UserResponseDto userDto;
    private Penalty penaltyEntity;

    @BeforeEach
    void setUp() {
        requestDto = new PenaltyRequestDto();
        requestDto.setUserRut("11111111-1");
        requestDto.setAmount(5000);
        requestDto.setReason("Atraso en devolución");

        userDto = new UserResponseDto();
        userDto.setUserRut("11111111-1");
        userDto.setUserName("Juan Perez");

        penaltyEntity = new Penalty();
        penaltyEntity.setIdPenalty(1L);
        penaltyEntity.setUserRut("11111111-1");
        penaltyEntity.setAmount(5000);
        penaltyEntity.setReason("Atraso en devolución");
        penaltyEntity.setStatus("pendiente");
        penaltyEntity.setCreatedAt(LocalDate.now());
    }

    @Test
    void save_Success() {
        // Arrange
        when(userClient.getUserByRut(anyString())).thenReturn(userDto);
        when(repository.save(any(Penalty.class))).thenReturn(penaltyEntity);

        // Act
        PenaltyResponseDto result = service.save(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdPenalty());
        assertEquals("pendiente", result.getStatus());
        verify(repository, times(1)).save(any(Penalty.class));
    }

    @Test
    void save_UserNotFound_ThrowsException() {
        // Arrange: Simulamos que el UserClient no encuentra a nadie (retorna null)
        when(userClient.getUserByRut(anyString())).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.save(requestDto);
        });

        assertTrue(exception.getMessage().contains("usuario no encontrado"));
        // Verificamos que NUNCA intentó guardar la multa en BD
        verify(repository, never()).save(any(Penalty.class));
    }

    @Test
    void findByRut_Success() {
        // Arrange
        when(repository.findByUserRut(anyString())).thenReturn(List.of(penaltyEntity));

        // Act
        List<PenaltyResponseDto> result = service.findByRut("11111111-1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5000, result.get(0).getAmount());
    }

    @Test
    void findByRut_EmptyList_ThrowsException() {
        // Arrange: La base de datos no tiene multas para ese RUT
        when(repository.findByUserRut(anyString())).thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.findByRut("99999999-9");
        });

        assertTrue(exception.getMessage().contains("usuario no encontrado"));
    }
}