package cl.duocuc.penalty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyResponseDto {

    private Long idPenalty;
    private String userRut;
    private Integer amount;
    private String reason;
    private String status;
    private LocalDate createdAt;
}
