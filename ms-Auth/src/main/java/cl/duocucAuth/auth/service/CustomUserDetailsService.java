package cl.duocucAuth.auth.service;

import cl.duocucAuth.auth.model.AuthUser;
import cl.duocucAuth.auth.repository.IAuthUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IAuthUserRepository authUserRepository;

    public CustomUserDetailsService(IAuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado -" + username + "-"));

        return new User(authUser.getUsername(), authUser.getPassword(), getAuthorities(authUser));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(AuthUser authUser) {
        // Formateamos el rol a mayúsculas y le agregamos ROLE_ si no lo tiene
        String role = authUser.getRole().toUpperCase();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}