package com.yuri.WikichefBckE.controller.secured;

import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/private/v1/users")
@RequiredArgsConstructor
public class PrivateUserController {

    private final UserService userService;

    /**
     * Crear usuario
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> crearUsuario(
            @RequestBody UserDTO userDTO) {
        UserDTO creado = userService.crear(userDTO);
        return ResponseEntity.ok(creado);
    }

    /**
     * Listar usuarios
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listarUsuarios() {
        return ResponseEntity.ok(userService.listar());
    }

    /**
     * Obtener usuario por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> obtenerUsuario(
            @PathVariable Integer id) {
        return ResponseEntity.ok(userService.obtenerPorId(id));
    }

    /**
     * Actualizar usuario
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(
                userService.actualizar(id, userDTO)
        );
    }

    /**
     * Eliminar usuario
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Integer id) {
        userService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

