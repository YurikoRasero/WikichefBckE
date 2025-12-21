package com.yuri.WikichefBckE.controller.secured;

import com.yuri.WikichefBckE.service.CalificacionService;
import com.yuri.WikichefBckE.dto.CalificacionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/private/v1")
@RequiredArgsConstructor
public class PrivateCalificacionController {

    private final CalificacionService service;

    /**
     * Crear calificacion para una receta
     */
    @PostMapping("/recetas/{recetaId}/calificaciones")
    public ResponseEntity<CalificacionDTO> crear(
            @PathVariable Integer recetaId,
            @RequestBody CalificacionDTO dto) {
        return ResponseEntity.ok(service.crear(dto, recetaId));
    }

    /**
     * Actualizar calificacion
     */
    @PutMapping("/calificaciones/{id}")
    public ResponseEntity<CalificacionDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody CalificacionDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    /**
     * Eliminar calificacion
     */
    @DeleteMapping("/calificaciones/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

