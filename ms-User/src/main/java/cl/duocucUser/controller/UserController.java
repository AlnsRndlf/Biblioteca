package cl.duocucUser.controller;

import cl.duocucUser.dto.UserDto;
import cl.duocucUser.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuarios", description = "Endpoints para la gestión de usuarios y membresías de la biblioteca")
public class UserController {

    private final IUserService service;

    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista completa con todos los usuarios registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("Solicitud recibida para obtener todos los usuarios.");
        List<UserDto> users = service.findAll();
        log.info("Se retornaron {} usuarios encontrados.", users.size());
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Buscar usuario por RUT", description = "Busca un usuario específico utilizando su RUT (con guion y dígito verificador).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El usuario con el RUT indicado no existe")
    })
    @GetMapping("/{rut}")
    public ResponseEntity<UserDto> findByRut(
            @Parameter(description = "RUT del usuario (Ej: 11111111-1)", required = true)
            @PathVariable String rut) {
        log.info("Solicitud recibida para buscar al usuario con RUT: {}", rut);
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @Operation(summary = "Buscar usuario por Email", description = "Busca un usuario específico utilizando su dirección de correo electrónico.")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> findByEmail(
            @Parameter(description = "Correo electrónico del usuario", required = true)
            @PathVariable String email) {
        log.info("Solicitud recibida para buscar al usuario por email: {}", email);
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario en la base de datos validando que el RUT y el Email no estén duplicados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "RUT o Email ya se encuentran registrados en el sistema")
    })
    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        log.info("Solicitud recibida para registrar un nuevo usuario: {} (RUT: {})", userDto.getFullName(), userDto.getRut());
        UserDto savedUser = service.save(userDto);
        log.info("Usuario registrado exitosamente respondiendo al cliente. RUT: {}", savedUser.getRut());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(summary = "Actualizar nombre del usuario", description = "Modifica únicamente el nombre completo de un usuario existente.")
    @PatchMapping("/{rut}/cambioNombre/{newFullName}")
    public ResponseEntity<UserDto> updateFullname(
            @Parameter(description = "RUT del usuario a modificar", required = true) @PathVariable String rut,
            @Parameter(description = "Nuevo nombre completo", required = true) @Valid @PathVariable("newFullName") String newFullname) {
        log.info("Solicitud recibida para actualizar el nombre del usuario RUT: {}. Nuevo nombre: {}", rut, newFullname);
        UserDto updated = service.updateFullName(rut, newFullname);
        log.info("Nombre actualizado exitosamente para el RUT: {}", rut);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Actualizar email del usuario", description = "Modifica únicamente el correo electrónico de un usuario existente.")
    @PatchMapping("/{rut}/cambioEmail/{newEmail}")
    public ResponseEntity<UserDto> updateEmail(
            @Parameter(description = "RUT del usuario a modificar", required = true) @PathVariable String rut,
            @Parameter(description = "Nueva dirección de correo electrónico", required = true) @Valid @PathVariable("newEmail") String newEmail) {
        log.info("Solicitud recibida para actualizar el email del usuario RUT: {}. Nuevo email: {}", rut, newEmail);
        UserDto updated = service.updateEmail(rut, newEmail);
        log.info("Email actualizado exitosamente para el RUT: {}", rut);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina de forma permanente un usuario del sistema utilizando su RUT.")
    @DeleteMapping("/{rut}")
    public ResponseEntity<String> deleteByRut(
            @Parameter(description = "RUT del usuario a eliminar", required = true)
            @PathVariable String rut) {
        log.info("Solicitud recibida para eliminar al usuario con RUT: {}", rut);
        service.deleteByRut(rut);
        log.info("Usuario con RUT {} eliminado. Respondiendo al cliente.", rut);
        return ResponseEntity.ok("usuario eliminado correctamente.");
    }
}