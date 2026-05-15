package cl.duocucLoan.loan.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data @NoArgsConstructor @AllArgsConstructor
public class UserResponseDto {

    @JsonProperty("rut")
    private String Userut;

    @JsonProperty("fullName")
    private String userName;

    @JsonProperty("email")
    private String UserEmail;

    @JsonProperty("membershipedDate")
    private LocalDate UserMembershipedDate;

}

