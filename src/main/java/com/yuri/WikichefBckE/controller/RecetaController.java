package com.yuri.WikichefBckE.controller;

import com.yuri.WikichefBckE.Service.RecetaService;
import com.yuri.WikichefBckE.dto.RecetaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yuri.WikichefBckE.Service.RecetaService.listar;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
public class RecetaController {

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
     * Listar todas las recetas
     */
    @GetMapping
    public ResponseEntity<List<RecetaDTO>> listarRecetas() {
        return ResponseEntity.ok(listar());
    }

    /**
     * Obtener receta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecetaDTO> obtenerReceta(
            @PathVariable Integer id) {
        return ResponseEntity.ok(RecetaService.obtenerPorId(id));
    }

    /**
     * Actualizar receta
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecetaDTO> actualizarReceta(
            @PathVariable Integer id,
            @RequestBody RecetaDTO recetaDTO) {
        return ResponseEntity.ok(
                RecetaService.actualizar(id, recetaDTO)
        );
    }

    /**
     * Eliminar receta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReceta(
            @PathVariable Integer id) {
        RecetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
