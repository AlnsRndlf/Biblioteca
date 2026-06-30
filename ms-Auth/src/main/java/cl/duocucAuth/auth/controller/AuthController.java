package cl.duocucAuth.auth.controller;

import cl.duocucAuth.auth.dto.AuthRequestDto;
import cl.duocucAuth.auth.dto.AuthResponseDto;
import cl.duocucAuth.auth.model.AuthUser;
import cl.duocucAuth.auth.repository.IAuthUserRepository;
import cl.duocucAuth.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
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
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final IAuthUserRepository authUserRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public AuthUser register(@RequestBody AuthUser user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("USER");
        }

        return authUserRepository.save(user);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        AuthUser user = authUserRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponseDto(token);
    }
}