package com.yuri.WikichefBckE.Service;

import com.yuri.WikichefBckE.dto.CalificacionDTO;
import com.yuri.WikichefBckE.modelo.Calificacion;
import com.yuri.WikichefBckE.modelo.Receta;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.repository.CalificacionRepository;
import com.yuri.WikichefBckE.repository.RecetaRepository;
import com.yuri.WikichefBckE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository repo;
    private final UserRepository userRepo;
    private final RecetaRepository recetaRepo;

    public Calificacion puntuar(String username, Integer recetaId, int puntos) {
        User u = (User) userRepo.findByUsername(username).orElseThrow();
        Receta r = recetaRepo.findById(recetaId).orElseThrow();

        Optional<Calificacion> existing = repo.findByUsuarioIdAndRecetaId(u.getId(), recetaId);

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

    public void eliminar(Integer id) {

    }


    public @Nullable CalificacionDTO crear(CalificacionDTO dto) {
            return null;
    }

    public @Nullable List<CalificacionDTO> listar() {
        return null;
    }

    public @Nullable CalificacionDTO obtenerPorId(Integer id) {
        return null;
    }

    public @Nullable CalificacionDTO actualizar(Integer id, CalificacionDTO dto) {
        return null;
    }
}
