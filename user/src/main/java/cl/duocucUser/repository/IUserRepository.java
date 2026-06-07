package cl.duocucUser.repository;

import cl.duocucUser.dto.UserDto;
import cl.duocucUser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    UserDto findByRut(String rut);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
