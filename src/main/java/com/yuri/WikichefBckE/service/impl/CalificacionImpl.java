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
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalificacionImpl implements CalificacionService {

    private final CalificacionRepository repo;
    private final UserRepository userRepo;
    private final RecetaRepository recetaRepo;

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

    public @Nullable CalificacionDTO crear(CalificacionDTO dto) {
        return null;
    }

    @Override
    public List<CalificacionDTO> listar() {
        return List.of();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return null;
    }

    @Override
    public CalificacionDTO obtenerPorId(Integer id) {
        return null;
    }

    @Override
    public CalificacionDTO actualizar(Integer id, CalificacionDTO calificacionDTO) {
        return null;
    }

    @Override
    public void eliminar(Integer id) {

    }

}
