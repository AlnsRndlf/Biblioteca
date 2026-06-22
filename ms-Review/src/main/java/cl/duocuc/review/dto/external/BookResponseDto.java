package cl.duocuc.review.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private Long bookIsbn;
    private String bookTitle;
    private String bookAuthor;
}
