package cl.duocucUser.Service;

import cl.duocucUser.Model.User;


import java.util.List;
import java.util.Optional;


public interface IUserService {
    List<User> findAll();
    Optional<User> findByRut(String rut);
}
