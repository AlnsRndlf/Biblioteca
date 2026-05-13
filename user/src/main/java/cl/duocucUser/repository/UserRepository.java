package cl.duocucUser.repository;

import cl.duocucUser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByRut(String rut);
    User findByEmail(String email);
}
