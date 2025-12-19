package com.yuri.WikichefBckE.controller;

import com.yuri.WikichefBckE.service.impl.ComentarioImpl;
import com.yuri.WikichefBckE.dto.ComentarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioImpl comentarioService;

    /**
     * Crear comentario
     */
    @PostMapping
    public ResponseEntity<ComentarioDTO> crearComentario(
            @RequestBody ComentarioDTO comentarioDTO) {
        ComentarioDTO creado = comentarioService.crear(comentarioDTO);
        return ResponseEntity.ok(creado);
    }

    /**
     * Listar todos los comentarios
     */
    @GetMapping
    public ResponseEntity<List<ComentarioDTO>> listarComentarios() {
        return ResponseEntity.ok(comentarioService.listar());
    }

    /**
     * Obtener comentario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDTO> obtenerComentario(
            @PathVariable Integer id) {
        return ResponseEntity.ok(comentarioService.obtenerPorId(id));
    }

    /**
     * Actualizar comentario
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComentarioDTO> actualizarComentario(
            @PathVariable Integer id,
            @RequestBody ComentarioDTO comentarioDTO) {
        return ResponseEntity.ok(
                comentarioService.actualizar(id, comentarioDTO)
        );
    }

    /**
     * Eliminar comentario
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(
            @PathVariable Integer id) {
        comentarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
