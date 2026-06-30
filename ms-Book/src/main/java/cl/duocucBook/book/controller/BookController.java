package cl.duocucBook.book.controller;

import cl.duocucBook.book.dto.BookDto;
import cl.duocucBook.book.service.IBookService;
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
public class BookController {

    private final IBookService service;

    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        log.info("Solicitud recibida para obtener todos los libros.");
        List<BookDto> books = service.findAll();
        log.info("Se retornaron {} libros encontrados.", books.size());
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable Long isbn) {
        log.info("Solicitud recibida para buscar el libro con ISBN: {}", isbn);
        return ResponseEntity.ok(service.findByIsbn(isbn));
    }

    @GetMapping("/buscar/{title}")
    public ResponseEntity<BookDto> findByTitle(@PathVariable String title) {
        log.info("Solicitud recibida para buscar el libro por título: {}", title);
        return ResponseEntity.ok(service.findByTitle(title));
    }

    @PostMapping
    public ResponseEntity<BookDto> save(@Valid @RequestBody BookDto bookDTO) {
        log.info("Solicitud recibida para guardar un nuevo libro: {}", bookDTO.getTitle());
        BookDto savedBook = service.save(bookDTO);
        log.info("Libro guardado exitosamente respondiendo al cliente. ISBN: {}", savedBook.getIsbn());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> delete(@PathVariable Long isbn) {
        log.info("Solicitud recibida para eliminar el libro con ISBN: {}", isbn);
        service.deleteByIsbn(isbn);
        log.info("Libro con ISBN {} eliminado. Respondiendo al cliente.", isbn);
        return ResponseEntity.ok("Libro eliminado correctamente.");
    }

    @PatchMapping("/{isbn}/stock/{quantity}")
    public ResponseEntity<BookDto> updateStock(@PathVariable("isbn") Long isbn, @PathVariable("quantity") int quantity) {
        log.info("Solicitud recibida para actualizar el stock del libro ISBN: {}. Cantidad a ajustar: {}", isbn, quantity);
        BookDto updatedBook = service.updateStock(isbn, quantity);
        log.info("Stock actualizado exitosamente para el libro ISBN: {}", isbn);
        return ResponseEntity.ok(updatedBook);
    }
}