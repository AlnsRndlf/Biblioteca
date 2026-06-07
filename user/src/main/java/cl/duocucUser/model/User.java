package cl.duocucUser.model;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;

@Entity @Table(name = "app_user")
@Data
@AllArgsConstructor @NoArgsConstructor

public class User {
    @Id
    private String rut;

    @Column(name = "full_name")
    private String fullName;

    private String email;

    @Column(name = "membershiped_date")
    private LocalDate membershipedDate;
}
