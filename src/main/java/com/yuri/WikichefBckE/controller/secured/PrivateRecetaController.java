package com.yuri.WikichefBckE.controller.secured;

import com.yuri.WikichefBckE.service.RecetaService;
import com.yuri.WikichefBckE.dto.RecetaDTO;
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
@RequestMapping("/api/private/v1/recetas")
@RequiredArgsConstructor
public class PrivateRecetaController {

    private final RecetaService recetaService;

    /**
     * Crear receta
     */
    @PostMapping
    public ResponseEntity<RecetaDTO> crearReceta(
            @RequestBody RecetaDTO recetaDTO) {
        RecetaDTO creada = recetaService.crear(recetaDTO);
        return ResponseEntity.ok(creada);
    }

    /**
     * Actualizar receta
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecetaDTO> actualizarReceta(
            @PathVariable Integer id,
            @RequestBody RecetaDTO recetaDTO) {
        return ResponseEntity.ok(recetaService.actualizar(id, recetaDTO));
    }

    /**
     * Eliminar receta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReceta(
            @PathVariable Integer id) {
        recetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

