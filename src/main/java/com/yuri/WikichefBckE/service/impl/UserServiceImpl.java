package com.yuri.WikichefBckE.service.impl;

import com.yuri.WikichefBckE.service.UserService;
import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsServiceInstance;
    
    @Override
    public UserDetailsService userDetailsService() {
        if (userDetailsServiceInstance == null) {
            userDetailsServiceInstance = new UserDetailsService() {
                @Override
                public UserDetails loadUserByUsername(String username)  {
                    return userRepository.findByEmail(username)
                            .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
                }
            };
        }
        return userDetailsServiceInstance;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDTO crear(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public List<UserDTO> listar() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO obtenerPorId(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return mapToDTO(user);
    }

    @Override
    public UserDTO actualizar(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }
        
        // Only update password if provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    @Override
    public void eliminar(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        // Don't expose password in DTO
        dto.setPassword(null);
        return dto;
    }
}
