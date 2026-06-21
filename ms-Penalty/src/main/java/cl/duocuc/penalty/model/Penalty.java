package cl.duocuc.penalty.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "penalties")
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id_penalty")
    private Long idPenalty;

    @Column(name = "user_rut",nullable = false)
    private String userRut;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, name ="created_at")
    private LocalDate createdAt;
}
