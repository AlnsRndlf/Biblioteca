package cl.duocucUser.service;

import cl.duocucUser.dto.UserDto;
import cl.duocucUser.model.User;
import cl.duocucUser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository repository;

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
    public Optional<UserDto> findByRut(String rut) {
        return repository.findById(rut).map(this::toDto);
    }

    @Override
    public UserDto save(UserDto userDto) {
        if(repository.existsById(userDto.getRut())) {
            throw new IllegalArgumentException("ya existe el usuario");
        }
        return this.toDto(repository.save(this.toEntity(userDto)));
    }

    @Override
    public void deleteByRut(String rut) {
        if(repository.existsById(rut)) {
            repository.deleteById(rut);
            return;
        }
        throw new IllegalArgumentException("el usuario no existe");
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        User user = repository.findByEmail(email);
        if(user != null) {
            return Optional.of(this.toDto(user));
        }
        else  {
            return Optional.empty();
        }
    }

    @Override
    public UserDto updateEmail(String rut, String newEmail) {
        User user = repository.findByRut(rut);
        if(user != null) {
            user.setEmail(newEmail);
            repository.save(user);
            return  this.toDto(user);
        }
        else  {
            throw new IllegalArgumentException("el usuario no existe");
        }
    }

    @Override
    public UserDto updateFullName(String rut, String newFullName) {
        User user = repository.findByRut(rut);
        if (user != null) {
            user.setFullName(newFullName);
            repository.save(user);
            return this.toDto(user);
        } else {
            throw new IllegalArgumentException("el usuario no existe");
        }
    }
}