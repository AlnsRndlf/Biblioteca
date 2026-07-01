package cl.duocucAuth.auth.controller;

import cl.duocucAuth.auth.dto.AuthRequestDto;
import cl.duocucAuth.auth.dto.AuthResponseDto;
import cl.duocucAuth.auth.model.AuthUser;
import cl.duocucAuth.auth.repository.IAuthUserRepository;
import cl.duocucAuth.auth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Seguridad (Auth)", description = "Endpoints públicos para el registro de credenciales y generación de Tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final IAuthUserRepository authUserRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Crear credenciales de usuario", description = "Registra un nuevo usuario con contraseña cifrada (BCrypt) en la base de datos de seguridad. Si no se especifica rol, se asigna 'USER' por defecto.")
    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente")
    @PostMapping("/register")
    public AuthUser register(@RequestBody AuthUser user) {
        log.info("Intentando registrar nuevo usuario: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            log.info("Asignando rol por defecto 'USER' al usuario: {}", user.getUsername());
            user.setRole("USER");
        }
        AuthUser savedUser = authUserRepository.save(user);
        log.info("Usuario {} registrado exitosamente.", savedUser.getUsername());
        return savedUser;
    }

    @Operation(summary = "Iniciar sesión (Generar Token)", description = "Valida las credenciales del usuario y retorna un Token JWT con los permisos (RBAC) correspondientes.")
    @ApiResponse(responseCode = "200", description = "Login exitoso, retorna el Token JWT")
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        log.info("Intento de login para el usuario: {}", request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        AuthUser user = authUserRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        log.info("Login exitoso y token generado para el usuario: {}", user.getUsername());
        return new AuthResponseDto(token);
    }
}