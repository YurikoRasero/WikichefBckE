package com.yuri.WikichefBckE.controller;

import com.yuri.WikichefBckE.Service.CalificacionService;
import com.yuri.WikichefBckE.dto.CalificacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class CalificacionController {

    private final CalificacionService service;

    public CalificacionController(CalificacionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CalificacionDTO> crear(
            @RequestBody CalificacionDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<CalificacionDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> obtener(
            @PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody CalificacionDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}