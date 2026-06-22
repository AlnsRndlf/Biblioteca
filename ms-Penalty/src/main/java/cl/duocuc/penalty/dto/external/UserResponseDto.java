package cl.duocuc.penalty.dto.external;


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
