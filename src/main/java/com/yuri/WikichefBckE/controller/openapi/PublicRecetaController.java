package com.yuri.WikichefBckE.controller.openapi;

import com.yuri.WikichefBckE.dto.RecetaDTO;
import com.yuri.WikichefBckE.service.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/v1/recetas")
@RequiredArgsConstructor
public class PublicRecetaController {

    private final RecetaService recetaService;

    /**
     * Listar todas las recetas
     */
    @GetMapping
    public ResponseEntity<List<RecetaDTO>> listarRecetas() {
        return ResponseEntity.ok(recetaService.listar());
    }

    /**
     * Obtener receta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecetaDTO> obtenerReceta(
            @PathVariable Integer id) {
        return ResponseEntity.ok(recetaService.obtenerPorId(id));
    }
}

