package com.yuri.WikichefBckE.service;

import com.yuri.WikichefBckE.dto.ComentarioDTO;
import com.yuri.WikichefBckE.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface ComentarioService {
    UserDetailsService userDetailsService();

    ComentarioDTO crear(ComentarioDTO comentarioDTO, Integer recetaId);
    List<ComentarioDTO> listar();
    ComentarioDTO obtenerPorId(Integer id);
    ComentarioDTO actualizar(Integer id, ComentarioDTO comentarioDTO);
    void eliminar(Integer id);

}
