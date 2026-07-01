package cl.duocucUser.service;

import cl.duocucUser.dto.UserDto;
import cl.duocucUser.model.User;
import cl.duocucUser.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Le decimos a JUnit 5 que use Mockito
class UserServiceImplTest {

    @Mock
    private IUserRepository repository; // Base de datos falsa (Mock)

    @InjectMocks
    private UserServiceImpl service; // El servicio REAL que vamos a probar

    private User userEntity;
    private UserDto userDto;

    // Este método se ejecuta antes de cada prueba para preparar datos frescos
    @BeforeEach
    void setUp() {
        userEntity = new User("11111111-1", "Juan Perez", "juan@correo.com", LocalDate.now());
        userDto = new UserDto("11111111-1", "Juan Perez", "juan@correo.com", LocalDate.now());
    }

    @Test
    void save_Success() {
        // 1. Arrange (Preparar): Le decimos al Mock cómo comportarse
        when(repository.existsById(anyString())).thenReturn(false); // RUT no existe
        when(repository.existsByEmail(anyString())).thenReturn(false); // Email no existe
        when(repository.save(any(User.class))).thenReturn(userEntity); // Al guardar, retorna la entidad

        // 2. Act (Actuar): Ejecutamos nuestro método real
        UserDto result = service.save(userDto);

        // 3. Assert (Comprobar): Verificamos que el resultado sea el esperado
        assertNotNull(result);
        assertEquals("11111111-1", result.getRut());
        assertEquals("Juan Perez", result.getFullName());
        // Verificamos que el repositorio falso haya sido llamado exactamente 1 vez
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void save_RutAlreadyExists_ThrowsException() {
        // 1. Arrange: Simulamos que el RUT ya existe en la BD
        when(repository.existsById(anyString())).thenReturn(true);

        // 2 & 3. Act & Assert: Comprobamos que ESTALLE la excepción correcta
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.save(userDto);
        });

        // Verificamos que el mensaje de error sea el correcto
        assertTrue(exception.getMessage().contains("ya esta registrado"));
        // Verificamos que NUNCA se haya intentado guardar en la BD
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void findByRut_Success() {
        // Arrange
        when(repository.findById("11111111-1")).thenReturn(Optional.of(userEntity));

        // Act
        UserDto result = service.findByRut("11111111-1");

        // Assert
        assertNotNull(result);
        assertEquals("juan@correo.com", result.getEmail());
    }

    @Test
    void findByRut_NotFound_ThrowsException() {
        // Arrange: Simulamos que la BD devuelve vacío
        when(repository.findById("99999999-9")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findByRut("99999999-9");
        });

        assertEquals("rut 99999999-9 no encontrado", exception.getMessage());
    }
}