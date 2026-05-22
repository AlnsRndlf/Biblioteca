package cl.duocuc.review.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="reviews")
public class Review {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Long idReview;

    @Column(nullable = false,name = "user_rut")
    private String userRut;

    @Column(nullable = false,name="book_isbn")
    private Long bookIsbn;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String comment;

    @Column(name="created_at")
    private LocalDate createdAt;
}
