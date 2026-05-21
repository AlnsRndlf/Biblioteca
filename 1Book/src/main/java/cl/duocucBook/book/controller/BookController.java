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
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final IBookService service;

    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable Long isbn) {
        Optional<BookDto> book = service.findByIsbn(isbn);
        if(book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }
        else {
            throw new IllegalArgumentException("libro de isbn: " + isbn + " no encontrado");
        }
    }

    @GetMapping("/buscar/{title}")
    public ResponseEntity<BookDto> findByTitle(@PathVariable("title") String title) {
        Optional<BookDto> book = service.findByTitle(title);
        if(book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }
        else {
            throw new IllegalArgumentException("libro de isbn: " + title + " no encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<BookDto> save(@Valid @RequestBody BookDto bookDTO) {
        BookDto saved = service.save(bookDTO);
        if(saved != null) {
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> delete(@PathVariable Long isbn) {
        service.deleteByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{isbn}/stock/{quantity}")
    public ResponseEntity<BookDto> updateStock(@PathVariable Long isbn, @PathVariable int quantity) {
            BookDto updated = service.updateStock(isbn, quantity);
            return ResponseEntity.ok(updated);
        }
}
