package cl.duocucLoan.loan.repository;

import cl.duocucLoan.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBookIsbn(Long bookIsbn);
    List<Loan> findByUserRut(String rut);

}
