package cl.duocucUser.Service;

import cl.duocucUser.DTO.UserDto;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    List<UserDto> findAll();
    Optional<UserDto> findByRut(String rut);
    Optional<UserDto> findByEmail(String email);
    UserDto save(UserDto userDto);
    Optional<UserDto> updateFullName(String rut, String newFullName);
    Optional<UserDto> updateEmail(String rut, String newEmail);
    void deleteByRut(String rut);
}
