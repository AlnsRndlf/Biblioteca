package cl.duocuc.review.repository;

import cl.duocuc.review.client.LoanClient;
import cl.duocuc.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookIsbn(String bookIsbn);
    List<LoanClient> findByUserRut(String rut);
}
