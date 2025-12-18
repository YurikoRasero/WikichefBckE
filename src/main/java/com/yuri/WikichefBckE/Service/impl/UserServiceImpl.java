package com.yuri.WikichefBckE.Service.impl;

import com.yuri.WikichefBckE.Service.UserService;
import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public void eliminar(Integer id) {

    }
}
