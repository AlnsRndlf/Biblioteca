package cl.duocucLoan.loan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="loans")
public class Loan {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name ="id_loan")
    private Long idLoan;

    @Column(name="book_isbn")
    private Long bookIsbn;

    @Column(name="user_rut")
    private String userRut;

    @Column(name="loan_date")
    private LocalDate loanDate;

    @Column(name="due_date")
    private LocalDate dueDate;

    @Column(name="returned_date")
    private LocalDate returnedDate;
}
