package com.yuri.WikichefBckE.controller;

import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


    private final UserService userService;

    /**
     * Crear usuario
     */
    @PostMapping
    public ResponseEntity<UserDTO> crearUsuario(
            @RequestBody UserDTO userDTO) {
        UserDTO creado = userService.crear(userDTO);
        return ResponseEntity.ok(creado);
    }

    /**
     * Listar usuarios
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> listarUsuarios() {
        return ResponseEntity.ok(userService.listar());
    }

    /**
     * Obtener usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> obtenerUsuario(
            @PathVariable Integer id) {
        ResponseEntity<UserDTO> ok = ResponseEntity.ok(userService.obtenerPorId(id));
        return ok;
    }

    /**
     * Actualizar usuario
     */
    @PutMapping("/{id}")
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
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Integer id) {
        userService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
