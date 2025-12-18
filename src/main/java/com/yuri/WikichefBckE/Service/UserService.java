package com.yuri.WikichefBckE.Service;

import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.modelo.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    User createUser(User user);

    static UserDTO crear(UserDTO userDTO);

    @Nullable
    static List<UserDTO> listar();

    @Nullable
    static UserDTO obtenerPorId(Integer id);

    @Nullable
    static UserDTO actualizar(Integer id, UserDTO userDTO);

    static void eliminar(Integer id);
}
