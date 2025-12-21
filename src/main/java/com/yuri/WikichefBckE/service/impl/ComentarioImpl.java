package com.yuri.WikichefBckE.service.impl;

import com.yuri.WikichefBckE.dto.ComentarioDTO;
import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.modelo.Comentario;
import com.yuri.WikichefBckE.modelo.Receta;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.repository.ComentarioRepository;
import com.yuri.WikichefBckE.repository.RecetaRepository;
import com.yuri.WikichefBckE.repository.UserRepository;
import com.yuri.WikichefBckE.service.ComentarioService;
import com.yuri.WikichefBckE.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComentarioImpl implements ComentarioService {

    private final ComentarioRepository repo;
    private final UserRepository userRepo;
    private final RecetaRepository recetaRepo;
    private final ModelMapper modelMapper;

    public Comentario comentar(String username, int recetaId, String texto) {
        User u = (User) userRepo.findByUsername(username).orElseThrow();
        Receta r = recetaRepo.findById(recetaId).orElseThrow();

        Comentario c = Comentario.builder()
                .texto(texto)
                .users(u)
                .receta(r)
                .build();

        return repo.save(c);
    }

    public List<Comentario> listar(int recetaId) {
        return repo.findByRecetaIdOrderByIdDesc(recetaId);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return null;
    }

    @Override
    public ComentarioDTO crear(ComentarioDTO comentarioDTO, Integer recetaId) {
        // Get authenticated user from SecurityContext
        User authenticatedUser = SecurityUtil.getAuthenticatedUser();
        
        // Get receta from repository
        Receta receta = recetaRepo.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + recetaId));
        
        Comentario comentario = modelMapper.map(comentarioDTO, Comentario.class);
        comentario.setFecha(LocalDateTime.now());
        // Set usuario from authenticated user - ignore any usuario in DTO
        comentario.setUsers(authenticatedUser);
        // Set receta from path parameter
        comentario.setReceta(receta);
        
        Comentario saved = repo.save(comentario);
        return mapToDTO(saved);
    }

    @Override
    public List<ComentarioDTO> listar() {
        List<Comentario> comentarios = repo.findAll();
        return comentarios.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ComentarioDTO obtenerPorId(Integer id) {
        Comentario comentario = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con id: " + id));
        return mapToDTO(comentario);
    }

    @Override
    public ComentarioDTO actualizar(Integer id, ComentarioDTO comentarioDTO) {
        Comentario comentario = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con id: " + id));
        
        // Validate that the authenticated user is the owner
        User authenticatedUser = SecurityUtil.getAuthenticatedUser();
        if (comentario.getUsers() == null || !comentario.getUsers().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("No autorizado: solo el autor puede actualizar el comentario");
        }
        
        comentario.setTexto(comentarioDTO.getTexto());
        // Do not allow changing usuario or receta - they remain the same
        
        Comentario updated = repo.save(comentario);
        return mapToDTO(updated);
    }

    @Override
    public void eliminar(Integer id) {
        Comentario comentario = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con id: " + id));
        repo.delete(comentario);
    }

    private ComentarioDTO mapToDTO(Comentario comentario) {
        ComentarioDTO dto = modelMapper.map(comentario, ComentarioDTO.class);
        if (comentario.getUsers() != null) {
            UserDTO userDTO = modelMapper.map(comentario.getUsers(), UserDTO.class);
            dto.setUsuario(userDTO);
        }
        return dto;
    }


}