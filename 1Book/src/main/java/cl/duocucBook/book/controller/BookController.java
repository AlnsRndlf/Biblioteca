package cl.duocucBook.book.controller;

import cl.duocucBook.book.dto.BookDto;
import cl.duocucBook.book.service.IBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
public class BookController {

    private final IBookService service;

    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable Long isbn) {
        return ResponseEntity.ok(service.findByIsbn(isbn));
    }

    @GetMapping("/buscar/{title}")
    public ResponseEntity<BookDto> findByTitle(@PathVariable String title) {
        return ResponseEntity.ok(service.findByTitle(title));
    }

    @PostMapping
    public ResponseEntity<BookDto> save(@Valid @RequestBody BookDto bookDTO) {
        return new ResponseEntity<>(service.save(bookDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> delete(@PathVariable Long isbn) {
        service.deleteByIsbn(isbn);
        return ResponseEntity.ok("Libro eliminado correctamente.");
    }

    @PutMapping("/{isbn}/stock/{quantity}")
    public ResponseEntity<BookDto> updateStock(@PathVariable("isbn") Long isbn, @PathVariable("quantity") int quantity) {
        return ResponseEntity.ok(service.updateStock(isbn, quantity));
    }
}
