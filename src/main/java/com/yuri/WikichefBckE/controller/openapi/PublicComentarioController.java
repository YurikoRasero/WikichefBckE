package com.yuri.WikichefBckE.controller.openapi;

import com.yuri.WikichefBckE.dto.ComentarioDTO;
import com.yuri.WikichefBckE.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/v1/comentarios")
@RequiredArgsConstructor
public class PublicComentarioController {

    private final ComentarioService comentarioService;

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
}

