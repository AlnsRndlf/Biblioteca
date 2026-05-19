package cl.duocuc.reservation.repository;

import cl.duocuc.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationRepository extends JpaRepository<Reservation, Long> {
    //List<Reservation> findByUserRut(String userRut);
    //List<Reservation> findByBookIsbnAndStatus(String bookIsbn, String status);
}
