package cl.duocucBook.book.service;

import cl.duocucBook.book.dto.BookDto;
import cl.duocucBook.book.model.Book;
import cl.duocucBook.book.repository.IBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto save(BookDto bookDto) {
        if(repository.existsById(bookDto.getIsbn())) {
            throw new IllegalArgumentException("ya existe un libro con el isbn: "+bookDto.getIsbn());
        }
        return this.toDto(repository.save(this.toEntity(bookDto)));
    }

    @Override
    public void deleteByIsbn(Long isbn) {
        if (!repository.existsById(isbn)) {
            throw new IllegalArgumentException("no se encontro el libro de isbn: " + isbn);
        }
        repository.deleteById(isbn);
    }

    @Override
    public BookDto findByIsbn(Long isbn) {
        return repository.findById(isbn)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("no existe el libro con el isbn: " + isbn));
    }

    @Override
    public BookDto findByTitle(String title) {
        Book book = repository.findByTitle(title);
        if (book == null) {
            throw new IllegalArgumentException("libro " +  title + " no encontrado");
        }
        return this.toDto(book);
    }

    @Override
    public BookDto updateStock(Long isbn, int quantity) {
        Book book = repository.findById(isbn).orElse(null);
        if(book != null) {
            int newStock = book.getStock() + quantity;
            if(newStock < 0) {
                throw new IllegalArgumentException("Stock no puede ser negativo");
            }
            book.setStock(newStock);
            return this.toDto(repository.save(book));
        }
        throw new IllegalArgumentException("Libro no encontrado");
    }
}
