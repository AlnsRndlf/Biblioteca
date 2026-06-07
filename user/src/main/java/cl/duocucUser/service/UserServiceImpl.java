package cl.duocucUser.service;

import cl.duocucUser.dto.UserDto;
import cl.duocucUser.model.User;
import cl.duocucUser.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;

    private UserDto toDto(User user) {
        return new UserDto(
                user.getRut(),
                user.getFullName(),
                user.getEmail(),
                user.getMembershipedDate()
        );
    }

    private User toEntity(UserDto userDto) {
        return new User(
                userDto.getRut(),
                userDto.getFullName(),
                userDto.getEmail(),
                userDto.getMembershipedDate()
        );
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public UserDto findByRut(String rut) {
        return repository.findById(rut)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("rut " + rut + " no encontrado"));
    }

    @Override
    public UserDto save(UserDto userDto) {
        if (repository.existsById(userDto.getRut())) {
            throw new IllegalArgumentException("el rut "+ userDto.getRut() +"  ya esta registrado");
        }
        if(repository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("el email "+userDto.getEmail()+" ya esta registrado");
        }
        return this.toDto(repository.save(this.toEntity(userDto)));
    }

    @Override
    public void deleteByRut(String rut) {
        if (repository.existsById(rut)) {
            repository.deleteById(rut);
            return;
        }
        throw new IllegalArgumentException("el usuario no existe");
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = repository.findByEmail(email);
        if (user != null) {
            return this.toDto(user);
        } else {
            throw new IllegalArgumentException("correo " + email + " no encontado");
        }
    }

    @Override
    public UserDto updateFullName(String rut, String newFullName) {
        User user = repository.findById(rut)
                .orElseThrow(() -> new IllegalArgumentException("el usuario no existe"));
        user.setFullName(newFullName);
        repository.save(user);
        return toDto(user);
    }

    @Override
    public UserDto updateEmail(String rut, String newEmail) {
        User user = repository.findById(rut)
                .orElseThrow(() -> new IllegalArgumentException("el usuario no existe"));

        user.setEmail(newEmail);
        repository.save(user);
        return toDto(user);
    }
}