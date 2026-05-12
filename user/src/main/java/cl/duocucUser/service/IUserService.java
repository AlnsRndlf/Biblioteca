package cl.duocucUser.service;

import cl.duocucUser.dto.UserDto;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    List<UserDto> findAll();
    Optional<UserDto> findByRut(String rut);
    Optional<UserDto> findByEmail(String email);
    UserDto save(UserDto userDto);
    UserDto updateFullName(String rut, String newFullName);
    UserDto updateEmail(String rut, String newEmail);
    void deleteByRut(String rut);
}
