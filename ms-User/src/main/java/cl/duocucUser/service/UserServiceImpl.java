package cl.duocucUser.service;

import cl.duocucUser.dto.UserDto;
import cl.duocucUser.model.User;
import cl.duocucUser.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("Consultando la base de datos para obtener todos los usuarios.");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public UserDto findByRut(String rut) {
        log.info("Buscando en BD al usuario con RUT: {}", rut);
        return repository.findById(rut)
                .map(user -> {
                    log.info("Usuario encontrado en BD: {}", user.getFullName());
                    return this.toDto(user);
                })
                .orElseThrow(() -> {
                    log.warn("Búsqueda fallida: No existe usuario con RUT {}", rut);
                    return new IllegalArgumentException("rut " + rut + " no encontrado");
                });
    }

    @Override
    public UserDto save(UserDto userDto) {
        log.info("Iniciando el proceso de guardado para el usuario RUT: {}", userDto.getRut());

        if (repository.existsById(userDto.getRut())) {
            log.warn("Intento de guardado fallido: El RUT {} ya está registrado.", userDto.getRut());
            throw new IllegalArgumentException("el rut "+ userDto.getRut() +"  ya esta registrado");
        }
        if(repository.existsByEmail(userDto.getEmail())) {
            log.warn("Intento de guardado fallido: El email {} ya está registrado.", userDto.getEmail());
            throw new IllegalArgumentException("el email "+userDto.getEmail()+" ya esta registrado");
        }

        User savedUser = repository.save(this.toEntity(userDto));
        log.info("Usuario guardado exitosamente en la base de datos con RUT: {}", savedUser.getRut());
        return this.toDto(savedUser);
    }

    @Override
    public void deleteByRut(String rut) {
        log.info("Iniciando eliminación del usuario con RUT: {}", rut);
        if (repository.existsById(rut)) {
            repository.deleteById(rut);
            log.info("Usuario con RUT {} eliminado exitosamente de la base de datos.", rut);
            return;
        }
        log.warn("Intento de eliminación fallido: No se encontró el usuario RUT {}", rut);
        throw new IllegalArgumentException("el usuario no existe");
    }

    @Override
    public UserDto findByEmail(String email) {
        log.info("Buscando en BD al usuario con email: {}", email);
        User user = repository.findByEmail(email);
        if (user != null) {
            log.info("Usuario encontrado en BD con email: {}", email);
            return this.toDto(user);
        } else {
            log.warn("Búsqueda fallida: Correo '{}' no encontrado", email);
            throw new IllegalArgumentException("correo " + email + " no encontado");
        }
    }

    @Override
    public UserDto updateFullName(String rut, String newFullName) {
        log.info("Iniciando actualización de nombre para el RUT: {}", rut);
        User user = repository.findById(rut)
                .orElseThrow(() -> {
                    log.warn("Actualización de nombre fallida: No se encontró el RUT {}", rut);
                    return new IllegalArgumentException("el usuario no existe");
                });

        user.setFullName(newFullName);
        repository.save(user);
        log.info("Nombre actualizado exitosamente en la base de datos para RUT: {}", rut);
        return toDto(user);
    }

    @Override
    public UserDto updateEmail(String rut, String newEmail) {
        log.info("Iniciando actualización de email para el RUT: {}", rut);
        User user = repository.findById(rut)
                .orElseThrow(() -> {
                    log.warn("Actualización de email fallida: No se encontró el RUT {}", rut);
                    return new IllegalArgumentException("el usuario no existe");
                });

        user.setEmail(newEmail);
        repository.save(user);
        log.info("Email actualizado exitosamente en la base de datos para RUT: {}", rut);
        return toDto(user);
    }
}