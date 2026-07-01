package cl.duocucBook.book.service;

import cl.duocucBook.book.dto.BookDto;
import cl.duocucBook.book.model.Book;
import cl.duocucBook.book.repository.IBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private IBookRepository repository; // La base de datos falsa

    @InjectMocks
    private BookServiceImpl service; // El servicio real de libros

    private Book bookEntity;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        // Preparamos un libro de prueba con 5 unidades de stock
        bookEntity = new Book();
        bookEntity.setIsbn(9781234567890L);
        bookEntity.setTitle("Spring Boot Avanzado");
        bookEntity.setAuthor("Autor Test");
        bookEntity.setYearPublicated(2024);
        bookEntity.setStock(5);

        bookDto = new BookDto();
        bookDto.setIsbn(9781234567890L);
        bookDto.setTitle("Spring Boot Avanzado");
        bookDto.setAuthor("Autor Test");
        bookDto.setYearPublicated(2024);
        bookDto.setStock(5);
    }

    @Test
    void save_Success() {
        // Arrange
        when(repository.existsById(anyLong())).thenReturn(false);
        when(repository.save(any(Book.class))).thenReturn(bookEntity);

        // Act
        BookDto result = service.save(bookDto);

        // Assert
        assertNotNull(result);
        assertEquals(9781234567890L, result.getIsbn());
        assertEquals("Spring Boot Avanzado", result.getTitle());
        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    void save_IsbnAlreadyExists_ThrowsException() {
        // Arrange: Simulamos que el ISBN ya está tomado
        when(repository.existsById(anyLong())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.save(bookDto);
        });

        assertNotNull(exception);
        // Verificamos que NUNCA intentó guardar en BD si el ISBN ya existía
        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void findByIsbn_Success() {
        // Arrange
        when(repository.findById(9781234567890L)).thenReturn(Optional.of(bookEntity));

        // Act
        BookDto result = service.findByIsbn(9781234567890L);

        // Assert
        assertNotNull(result);
        assertEquals("Autor Test", result.getAuthor());
        assertEquals(5, result.getStock());
    }

    @Test
    void findByIsbn_NotFound_ThrowsException() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findByIsbn(999999L);
        });

        assertTrue(exception.getMessage().contains("existe"));
    }

    @Test
    void updateStock_Success() {
        // Arrange: Libro existe y devolveremos la misma entidad después de guardarla
        when(repository.findById(9781234567890L)).thenReturn(Optional.of(bookEntity));
        when(repository.save(any(Book.class))).thenReturn(bookEntity);

        // Act: El libro tiene stock 5. Le restaremos 2 (quantity = -2)
        BookDto result = service.updateStock(9781234567890L, -2);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getStock()); // 5 - 2 = 3
        verify(repository, times(1)).save(any(Book.class));
    }
}