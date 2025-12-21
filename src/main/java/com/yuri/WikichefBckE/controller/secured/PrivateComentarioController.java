package com.yuri.WikichefBckE.controller.secured;

import com.yuri.WikichefBckE.service.ComentarioService;
import com.yuri.WikichefBckE.dto.ComentarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/private/v1")
@RequiredArgsConstructor
public class PrivateComentarioController {

    private final ComentarioService comentarioService;

    /**
     * Crear comentario para una receta
     */
    @PostMapping("/recetas/{recetaId}/comentarios")
    public ResponseEntity<ComentarioDTO> crearComentario(
            @PathVariable Integer recetaId,
            @RequestBody ComentarioDTO comentarioDTO) {
        ComentarioDTO creado = comentarioService.crear(comentarioDTO, recetaId);
        return ResponseEntity.ok(creado);
    }

    /**
     * Actualizar comentario
     */
    @PutMapping("/comentarios/{id}")
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
    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<Void> eliminarComentario(
            @PathVariable Integer id) {
        comentarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

