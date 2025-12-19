package com.yuri.WikichefBckE.service;

import com.yuri.WikichefBckE.dto.CalificacionDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CalificacionService {

    UserDetailsService userDetailsService();

    CalificacionDTO crear(CalificacionDTO calificacionDTO);
    List<CalificacionDTO> listar();
    CalificacionDTO obtenerPorId(Integer id);
    CalificacionDTO actualizar(Integer id,CalificacionDTO calificacionDTO);
    void eliminar(Integer id);


}
