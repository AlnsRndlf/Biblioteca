package cl.duocuc.reservation.dto.external;

import lombok.Data;

@Data
public class BookResponseDto {

    private Long bookIsbn;
    private Integer stock;

}
