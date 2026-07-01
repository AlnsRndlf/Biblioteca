package cl.duocucBook.book.controller;

import cl.duocucBook.book.dto.BookDto;
import cl.duocucBook.book.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Libros", description = "Endpoints para la gestión del inventario de libros en la biblioteca")
public class BookController {

    private final IBookService service;

    @Operation(summary = "Obtener todos los libros", description = "Retorna una lista completa con todos los libros registrados en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Lista de libros obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        log.info("Solicitud recibida para obtener todos los libros.");
        List<BookDto> books = service.findAll();
        log.info("Se retornaron {} libros encontrados.", books.size());
        return ResponseEntity.ok().body(books);
    }

    @Operation(summary = "Buscar un libro por ISBN", description = "Busca un libro específico utilizando su código ISBN único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El libro con el ISBN indicado no existe")
    })
    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(
            @Parameter(description = "Código ISBN del libro a buscar", required = true)
            @PathVariable Long isbn) {
        log.info("Solicitud recibida para buscar el libro con ISBN: {}", isbn);
        return ResponseEntity.ok(service.findByIsbn(isbn));
    }

    @Operation(summary = "Buscar un libro por Título", description = "Busca un libro utilizando su título exacto.")
    @GetMapping("/buscar/{title}")
    public ResponseEntity<BookDto> findByTitle(
            @Parameter(description = "Título del libro a buscar", required = true)
            @PathVariable String title) {
        log.info("Solicitud recibida para buscar el libro por título: {}", title);
        return ResponseEntity.ok(service.findByTitle(title));
    }

    @Operation(summary = "Registrar un nuevo libro", description = "Crea y guarda un nuevo libro en el sistema. Requiere validación de campos obligatorios.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados o ISBN duplicado")
    })
    @PostMapping
    public ResponseEntity<BookDto> save(@Valid @RequestBody BookDto bookDTO) {
        log.info("Solicitud recibida para guardar un nuevo libro: {}", bookDTO.getTitle());
        BookDto savedBook = service.save(bookDTO);
        log.info("Libro guardado exitosamente respondiendo al cliente. ISBN: {}", savedBook.getIsbn());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Operation(summary = "Eliminar un libro", description = "Elimina un libro de la base de datos utilizando su código ISBN.")
    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> delete(
            @Parameter(description = "Código ISBN del libro a eliminar", required = true)
            @PathVariable Long isbn) {
        log.info("Solicitud recibida para eliminar el libro con ISBN: {}", isbn);
        service.deleteByIsbn(isbn);
        log.info("Libro con ISBN {} eliminado. Respondiendo al cliente.", isbn);
        return ResponseEntity.ok("Libro eliminado correctamente.");
    }

    @Operation(summary = "Actualizar el stock de un libro", description = "Modifica la cantidad de ejemplares disponibles de un libro (permite sumar o restar stock).")
    @PatchMapping("/{isbn}/stock/{quantity}")
    public ResponseEntity<BookDto> updateStock(
            @Parameter(description = "ISBN del libro", required = true) @PathVariable("isbn") Long isbn,
            @Parameter(description = "Cantidad a sumar (positiva) o restar (negativa)", required = true) @PathVariable("quantity") int quantity) {
        log.info("Solicitud recibida para actualizar el stock del libro ISBN: {}. Cantidad a ajustar: {}", isbn, quantity);
        BookDto updatedBook = service.updateStock(isbn, quantity);
        log.info("Stock actualizado exitosamente para el libro ISBN: {}", isbn);
        return ResponseEntity.ok(updatedBook);
    }
}