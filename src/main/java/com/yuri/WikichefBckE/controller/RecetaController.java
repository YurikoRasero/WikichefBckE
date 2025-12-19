package com.yuri.WikichefBckE.controller;

import com.yuri.WikichefBckE.modelo.Receta;
import com.yuri.WikichefBckE.service.impl.RecetaImpl;
import com.yuri.WikichefBckE.dto.RecetaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaImpl recetaService;

    /**
     * Crear receta
     */
    @PostMapping
    public ResponseEntity<Receta> crearReceta(
            @RequestBody RecetaDTO recetaDTO) {
        Receta creada = recetaService.crear(recetaDTO);
        return ResponseEntity.ok(creada);
    }

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

    /**
     * Actualizar receta
     */
    @PutMapping("/{id}")
    public ResponseEntity<Receta> actualizarReceta(
            @PathVariable Integer id,
            @RequestBody RecetaDTO recetaDTO) {
        return ResponseEntity.ok(recetaService.actualizar(id, recetaDTO)
        );
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
