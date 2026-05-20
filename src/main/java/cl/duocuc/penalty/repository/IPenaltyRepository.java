package cl.duocuc.penalty.repository;


import cl.duocuc.penalty.model.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPenaltyRepository extends JpaRepository<Penalty, Long> {
    List<Penalty> findByUserRut(String userRut);
}
