package cl.duocucBook.book.service;

import cl.duocucBook.book.dto.BookDto;
import cl.duocucBook.book.model.Book;
import cl.duocucBook.book.repository.IBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements IBookService {

    private final IBookRepository repository;

    private BookDto toDto(Book book) {
        return new BookDto(
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getYearPublicated(),
                book.getStock()
        );
    }

    private Book toEntity(BookDto bookDTO) {
        return new Book(
                bookDTO.getIsbn(),
                bookDTO.getTitle(),
                bookDTO.getAuthor(),
                bookDTO.getYearPublicated(),
                bookDTO.getStock()
        );
    }

    // metodos del que implementa
    @Override
    public List<BookDto> findAll() {
        log.info("Consultando la base de datos para obtener todos los libros.");
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto save(BookDto bookDto) {
        log.info("Iniciando el proceso de guardado para el libro ISBN: {}", bookDto.getIsbn());
        if(repository.existsById(bookDto.getIsbn())) {
            log.warn("Intento de guardado fallido: Ya existe un libro con el ISBN {}", bookDto.getIsbn());
            throw new IllegalArgumentException("ya existe un libro con el isbn: "+bookDto.getIsbn());
        }
        Book savedBook = repository.save(this.toEntity(bookDto));
        log.info("Libro guardado exitosamente en la base de datos con ISBN: {}", savedBook.getIsbn());
        return this.toDto(savedBook);
    }

    @Override
    public void deleteByIsbn(Long isbn) {
        log.info("Iniciando eliminación del libro con ISBN: {}", isbn);
        if (!repository.existsById(isbn)) {
            log.warn("Intento de eliminación fallido: No se encontró el libro ISBN {}", isbn);
            throw new IllegalArgumentException("no se encontro el libro de isbn: " + isbn);
        }
        repository.deleteById(isbn);
        log.info("Libro con ISBN {} eliminado exitosamente de la base de datos.", isbn);
    }

    @Override
    public BookDto findByIsbn(Long isbn) {
        log.info("Buscando en BD el libro con ISBN: {}", isbn);
        return repository.findById(isbn)
                .map(book -> {
                    log.info("Libro encontrado en BD: {}", book.getTitle());
                    return this.toDto(book);
                })
                .orElseThrow(() -> {
                    log.warn("Búsqueda fallida: No existe libro con ISBN {}", isbn);
                    return new IllegalArgumentException("no existe el libro con el isbn: " + isbn);
                });
    }

    @Override
    public BookDto findByTitle(String title) {
        log.info("Buscando en BD el libro con título: {}", title);
        Book book = repository.findByTitle(title);
        if (book == null) {
            log.warn("Búsqueda fallida: Libro con título '{}' no encontrado", title);
            throw new IllegalArgumentException("libro " +  title + " no encontrado");
        }
        log.info("Libro encontrado en BD con título: {}", title);
        return this.toDto(book);
    }

    @Override
    public BookDto updateStock(Long isbn, int quantity) {
        log.info("Iniciando actualización de stock para el libro ISBN: {}. Cantidad recibida: {}", isbn, quantity);
        Book book = repository.findById(isbn).orElse(null);

        if(book != null) {
            int newStock = book.getStock() + quantity;
            log.info("Stock actual: {}. Nuevo stock calculado: {}", book.getStock(), newStock);

            if(newStock < 0) {
                log.warn("Actualización de stock fallida: El stock no puede ser negativo (intentó quedar en {})", newStock);
                throw new IllegalArgumentException("Stock no puede ser negativo");
            }

            book.setStock(newStock);
            Book updatedBook = repository.save(book);
            log.info("Stock actualizado exitosamente en la base de datos para ISBN: {}", isbn);
            return this.toDto(updatedBook);
        }

        log.warn("Actualización de stock fallida: No se encontró el libro ISBN {}", isbn);
        throw new IllegalArgumentException("Libro no encontrado");
    }
}