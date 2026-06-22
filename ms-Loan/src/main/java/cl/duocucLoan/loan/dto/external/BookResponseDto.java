package cl.duocucLoan.loan.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    @JsonProperty("isbn")
    private Long bookIsbn;

    @JsonProperty("title")
    private String bookTitle;

    @JsonProperty("author")
    private String bookAuthor;

}


