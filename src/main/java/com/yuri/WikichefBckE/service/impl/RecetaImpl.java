package com.yuri.WikichefBckE.service.impl;

import com.yuri.WikichefBckE.modelo.Receta;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.service.RecetaService;
import com.yuri.WikichefBckE.dto.RecetaDTO;
import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.repository.RecetaRepository;
import com.yuri.WikichefBckE.repository.UserRepository;
import com.yuri.WikichefBckE.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecetaImpl implements RecetaService {

    private final RecetaRepository repo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    @Override
    public void eliminar(Integer id) {
        Receta receta = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
        repo.delete(receta);
    }

    public void eliminar(int id, String username) {
        Receta r = repo.findById(id).orElseThrow();

        if (!r.getAutor().getUsername().equals(username))
            throw new RuntimeException("No autorizado");

        repo.delete(r);
    }

    public Page<Receta> buscar(String q, String ing, Pageable pageable) {

        if (q != null && !q.isBlank())
            return repo.findByTituloContainingIgnoreCase(q, pageable);

        if (ing != null && !ing.isBlank())
            return repo.findByIngredientesContainingIgnoreCase(ing, pageable);

        return repo.findAll(pageable);
    }
    public Receta get(int id) {
        return repo.findById(id).orElseThrow();
    }


    @Override
    public RecetaDTO crear(RecetaDTO recetaDTO) {
        // Get authenticated user from SecurityContext
        User authenticatedUser = SecurityUtil.getAuthenticatedUser();
        
        Receta receta = modelMapper.map(recetaDTO, Receta.class);
        // Set autor from authenticated user - ignore any autor in DTO
        receta.setAutor(authenticatedUser);
        receta.setCreatedAt(java.time.LocalDateTime.now());
        Receta saved = repo.save(receta);
        return mapToDTO(saved);
    }

    @Override
    public List<RecetaDTO> listar() {
        List<Receta> recetas = repo.findAll();
        return recetas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecetaDTO obtenerPorId(Integer id) {
        Receta receta = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
        return mapToDTO(receta);
    }

    private RecetaDTO mapToDTO(Receta receta) {
        RecetaDTO dto = modelMapper.map(receta, RecetaDTO.class);
        if (receta.getAutor() != null) {
            UserDTO autorDTO = modelMapper.map(receta.getAutor(), UserDTO.class);
            dto.setAutor(autorDTO);
        }
        return dto;
    }

    @Override
    public RecetaDTO actualizar(Integer id, RecetaDTO recetaDTO) {
        Receta r = repo.findById(id).orElseThrow();
        
        // Validate that the authenticated user is the owner
        User authenticatedUser = SecurityUtil.getAuthenticatedUser();
        if (!r.getAutor().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("No autorizado: solo el autor puede actualizar la receta");
        }
        
        r.setTitulo(recetaDTO.getTitulo());
        r.setDescripcion(recetaDTO.getDescripcion());
        r.setIngredientes(recetaDTO.getIngredientes());
        r.setEtiquetas(recetaDTO.getEtiquetas());
        // Do not allow changing autor - it remains the same

        Receta saved = repo.save(r);
        return mapToDTO(saved);
    }



}