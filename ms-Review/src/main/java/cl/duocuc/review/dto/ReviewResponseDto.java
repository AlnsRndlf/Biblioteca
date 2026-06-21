package cl.duocuc.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private Long idReview;
    private String userRut;
    private Long bookIsbn;
    private Integer rating;
    private String comment;
    private LocalDate created_at;
}
