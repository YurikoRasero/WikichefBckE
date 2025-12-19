package com.yuri.WikichefBckE.service.impl;

import com.yuri.WikichefBckE.service.UserService;
import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username)  {
                return userRepository.findByEmail(username)
                        .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
            }
        };
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDTO crear(UserDTO userDTO) {
        return null;
    }

    @Override
    public List<UserDTO> listar() {
        return List.of();
    }

    @Override
    public UserDTO obtenerPorId(Integer id) {
        return null;
    }

    @Override
    public UserDTO actualizar(Integer id, UserDTO userDTO) {
        return null;
    }

    @Override
    public void eliminar(Integer id) {

    }
}
