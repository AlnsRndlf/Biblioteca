package cl.duocuc.reservation.dto;

import lombok.Data;

@Data
public class BookResponseDto {

    private Long bookIsbn;
    private Integer stock;

}
