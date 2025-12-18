package com.yuri.WikichefBckE.Service;

import com.yuri.WikichefBckE.dto.ComentarioDTO;
import com.yuri.WikichefBckE.modelo.Comentario;
import com.yuri.WikichefBckE.modelo.Receta;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.repository.ComentarioRepository;
import com.yuri.WikichefBckE.repository.RecetaRepository;
import com.yuri.WikichefBckE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository repo;
    private final UserRepository userRepo;
    private final RecetaRepository recetaRepo;

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

    public ComentarioDTO crear(ComentarioDTO comentarioDTO) {
        return null;
    }

    public @Nullable ComentarioDTO obtenerPorId(Integer id) {
        return null;
    }

    public @Nullable ComentarioDTO actualizar(Integer id, ComentarioDTO comentarioDTO) {
        return null;
    }

    public void eliminar(Integer id) {

    }

    public @Nullable List<ComentarioDTO> listar() {
        return null;
    }
}