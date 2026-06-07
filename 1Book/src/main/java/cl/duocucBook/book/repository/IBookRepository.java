package cl.duocucBook.book.repository;


import cl.duocucBook.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book,Long> {
    Book findByTitle(String title);
}
