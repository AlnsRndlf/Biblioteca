package cl.duocucLoan.loan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    @JsonProperty("isbn")
    private Long Bookisbn;

    @JsonProperty("title")
    private String Booktitle;

    @JsonProperty("author")
    private String Bookauthor;

}


