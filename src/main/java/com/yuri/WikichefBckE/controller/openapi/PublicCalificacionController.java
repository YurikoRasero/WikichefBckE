package com.yuri.WikichefBckE.controller.openapi;

import com.yuri.WikichefBckE.dto.CalificacionDTO;
import com.yuri.WikichefBckE.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/public/v1/calificaciones")
@RequiredArgsConstructor
public class PublicCalificacionController {

    private final CalificacionService service;

    /**
     * Listar todas las calificaciones
     */
    @GetMapping
    public ResponseEntity<List<CalificacionDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Obtener calificacion por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> obtener(
            @PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}

