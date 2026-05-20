package cl.duocucUser.controller;

import cl.duocucUser.dto.UserDto;
import cl.duocucUser.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{rut}")
    public ResponseEntity<UserDto> findByRut(@PathVariable String rut) {
        Optional<UserDto> user = service.findByRut(rut);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else  {
            throw new IllegalArgumentException("el usuario no existe");
        }
    }

    /*
    @GetMapping("/{rut}")
    public ResponseEntity<UserDto> findByRut(@PathVariable String rut) {
        Optional<UserDto> user = service.findByRut(rut);
        return ResponseEntity.ok(user.get());
    }
     */

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        Optional<UserDto> user = service.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else  {
            throw new IllegalArgumentException("el usuario no existe");
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        UserDto savedUser = service.save(userDto);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @PatchMapping("/{rut}/cambioNombre/{newFullName}")
    public ResponseEntity <UserDto> updateFullname(@PathVariable String rut, @Valid @PathVariable("newFullName") String newFullname) {
        UserDto updated = service.updateFullName(rut,newFullname);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{rut}/cambioEmail/{newEmail}")
    public ResponseEntity<UserDto> updateEmail(@PathVariable String rut, @Valid @PathVariable("newEmail") String newEmail) {
        UserDto updated = service.updateEmail(rut, newEmail);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> deleteByRut(@PathVariable String rut) {
        service.deleteByRut(rut);
        return ResponseEntity.noContent().build();
    }
}
