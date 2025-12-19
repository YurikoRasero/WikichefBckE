package com.yuri.WikichefBckE.service;

import com.yuri.WikichefBckE.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService{

    UserDetailsService userDetailsService();

    UserDTO crear(UserDTO userDTO);
    List<UserDTO> listar();
    UserDTO obtenerPorId(Integer id);
    UserDTO actualizar(Integer id, UserDTO userDTO);
    void eliminar(Integer id);
}
