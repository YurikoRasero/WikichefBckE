package com.yuri.WikichefBckE.service;

import com.yuri.WikichefBckE.dto.RecetaDTO;

import java.util.List;

public interface RecetaService {

    RecetaDTO crear(RecetaDTO recetaDTO);
    List<RecetaDTO> listar();
    RecetaDTO obtenerPorId(Integer id);
    RecetaDTO actualizar(Integer id, RecetaDTO recetaDTO);
    void eliminar(Integer id);

}
