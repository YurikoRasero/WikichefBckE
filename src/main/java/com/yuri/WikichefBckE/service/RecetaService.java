package com.yuri.WikichefBckE.service;

import com.yuri.WikichefBckE.dto.RecetaDTO;
import com.yuri.WikichefBckE.modelo.Receta;

import java.util.List;

public interface RecetaService {

    Receta crear(RecetaDTO recetaDTO);
    List<RecetaDTO> listar();
    RecetaDTO obtenerPorId(Integer id);
    Receta actualizar(Integer id, RecetaDTO recetaDTO);
    void eliminar(Integer id);

}
