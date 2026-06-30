package cl.duocucUser.controller;

import cl.duocucUser.dto.UserDto;
import cl.duocucUser.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService service;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("Solicitud recibida para obtener todos los usuarios.");
        List<UserDto> users = service.findAll();
        log.info("Se retornaron {} usuarios encontrados.", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<UserDto> findByRut(@PathVariable String rut) {
        log.info("Solicitud recibida para buscar al usuario con RUT: {}", rut);
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        log.info("Solicitud recibida para buscar al usuario por email: {}", email);
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        log.info("Solicitud recibida para registrar un nuevo usuario: {} (RUT: {})", userDto.getFullName(), userDto.getRut());
        UserDto savedUser = service.save(userDto);
        log.info("Usuario registrado exitosamente respondiendo al cliente. RUT: {}", savedUser.getRut());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PatchMapping("/{rut}/cambioNombre/{newFullName}")
    public ResponseEntity<UserDto> updateFullname(@PathVariable String rut, @Valid @PathVariable("newFullName") String newFullname) {
        log.info("Solicitud recibida para actualizar el nombre del usuario RUT: {}. Nuevo nombre: {}", rut, newFullname);
        UserDto updated = service.updateFullName(rut, newFullname);
        log.info("Nombre actualizado exitosamente para el RUT: {}", rut);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{rut}/cambioEmail/{newEmail}")
    public ResponseEntity<UserDto> updateEmail(@PathVariable String rut, @Valid @PathVariable("newEmail") String newEmail) {
        log.info("Solicitud recibida para actualizar el email del usuario RUT: {}. Nuevo email: {}", rut, newEmail);
        UserDto updated = service.updateEmail(rut, newEmail);
        log.info("Email actualizado exitosamente para el RUT: {}", rut);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<String> deleteByRut(@PathVariable String rut) {
        log.info("Solicitud recibida para eliminar al usuario con RUT: {}", rut);
        service.deleteByRut(rut);
        log.info("Usuario con RUT {} eliminado. Respondiendo al cliente.", rut);
        return ResponseEntity.ok("usuario eliminado correctamente.");
    }
}