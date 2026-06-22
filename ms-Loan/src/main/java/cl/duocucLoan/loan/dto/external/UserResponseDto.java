package cl.duocucLoan.loan.dto.external;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data @NoArgsConstructor @AllArgsConstructor
public class UserResponseDto {

    @JsonProperty("rut")
    private String userRut;

    @JsonProperty("fullName")
    private String userName;

    @JsonProperty("email")
    private String userEmail;

    @JsonProperty("membershipedDate")
    private LocalDate userMembershipedDate;

}

