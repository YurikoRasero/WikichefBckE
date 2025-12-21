package com.yuri.WikichefBckE.service.impl;

import com.yuri.WikichefBckE.dto.CalificacionDTO;
import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.modelo.Calificacion;
import com.yuri.WikichefBckE.modelo.Receta;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.repository.CalificacionRepository;
import com.yuri.WikichefBckE.repository.RecetaRepository;
import com.yuri.WikichefBckE.repository.UserRepository;
import com.yuri.WikichefBckE.service.CalificacionService;
import com.yuri.WikichefBckE.service.UserService;
import com.yuri.WikichefBckE.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalificacionImpl implements CalificacionService {

    private final CalificacionRepository repo;
    private final UserRepository userRepo;
    private final RecetaRepository recetaRepo;
    private final ModelMapper modelMapper;

    public Calificacion puntuar(String username, Integer recetaId, int puntos) {
        User u = (User) userRepo.findByUsername(username).orElseThrow();
        Receta r = recetaRepo.findById(recetaId).orElseThrow();

        Optional<Calificacion> existing = repo.findByUsersIdAndRecetaId(u.getId(), recetaId);

        if (existing.isPresent()) {
            Calificacion calificacion = existing.get();
            calificacion.setPuntuacion(puntos);
            return repo.save(calificacion);
        }

        Calificacion newCalificacion = Calificacion.builder()
                .users(u)
                .receta(r)
                .puntuacion(puntos)
                .build();

        return repo.save(newCalificacion);
    }

    public double promedio(int recetaId) {
        List<Calificacion> arr = repo.findByRecetaId(recetaId);
        return arr.stream().mapToInt(Calificacion::getPuntuacion).average().orElse(0);
    }

    @Override
    public @Nullable CalificacionDTO crear(CalificacionDTO dto, Integer recetaId) {
        // Get authenticated user from SecurityContext
        User authenticatedUser = SecurityUtil.getAuthenticatedUser();
        
        // Get receta from repository using path parameter
        Receta receta = recetaRepo.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + recetaId));

        // Check if user already rated this recipe
        Optional<Calificacion> existing = repo.findByUsersIdAndRecetaId(authenticatedUser.getId(), recetaId);
        
        Calificacion calificacion;
        if (existing.isPresent()) {
            calificacion = existing.get();
            calificacion.setPuntuacion(dto.getPuntuacion());
        } else {
            calificacion = Calificacion.builder()
                    .users(authenticatedUser)  // Set from authenticated user - ignore usuarioId in DTO
                    .receta(receta)  // Set from path parameter - ignore recetaId in DTO
                    .puntuacion(dto.getPuntuacion())
                    .build();
        }
        
        Calificacion saved = repo.save(calificacion);
        return mapToDTO(saved);
    }

    @Override
    public List<CalificacionDTO> listar() {
        List<Calificacion> calificaciones = repo.findAll();
        return calificaciones.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailsService userDetailsService() {
        return null;
    }

    @Override
    public CalificacionDTO obtenerPorId(Integer id) {
        Calificacion calificacion = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificacion no encontrada con id: " + id));
        return mapToDTO(calificacion);
    }

    @Override
    public CalificacionDTO actualizar(Integer id, CalificacionDTO calificacionDTO) {
        Calificacion calificacion = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificacion no encontrada con id: " + id));
        
        // Validate that the authenticated user is the owner
        User authenticatedUser = SecurityUtil.getAuthenticatedUser();
        if (calificacion.getUsers() == null || !calificacion.getUsers().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("No autorizado: solo el autor puede actualizar la calificacion");
        }
        
        calificacion.setPuntuacion(calificacionDTO.getPuntuacion());
        // Do not allow changing usuario or receta - they remain the same
        
        Calificacion updated = repo.save(calificacion);
        return mapToDTO(updated);
    }

    @Override
    public void eliminar(Integer id) {
        Calificacion calificacion = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificacion no encontrada con id: " + id));
        repo.delete(calificacion);
    }

    private CalificacionDTO mapToDTO(Calificacion calificacion) {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(calificacion.getId());
        dto.setPuntuacion(calificacion.getPuntuacion());
        if (calificacion.getUsers() != null) {
            dto.setUsuarioId(calificacion.getUsers().getId());
        }
        if (calificacion.getReceta() != null) {
            dto.setRecetaId(calificacion.getReceta().getId());
        }
        return dto;
    }

}
