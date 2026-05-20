package cl.duocuc.penalty.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String userRut;
    private String userName;
    private String UserEmail;
}
