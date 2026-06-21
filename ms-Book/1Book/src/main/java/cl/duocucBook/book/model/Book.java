package cl.duocucBook.book.model;

import jakarta.persistence.*;
import lombok.*;
// rompi algo?
@Entity
@Data @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Book {

    @Id
    private Long isbn;

    private String title;
    private String author;

    @Column(name = "year_publicated")
    private Integer yearPublicated;
    private Integer stock;
}
