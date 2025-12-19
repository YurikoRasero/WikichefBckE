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
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioImpl implements ComentarioService {

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

    @Override
    public UserDetailsService userDetailsService() {
        return null;
    }

    public ComentarioDTO crear(ComentarioDTO comentarioDTO) {
        return null;
    }

    @Override
    public List<ComentarioDTO> listar() {
        return List.of();
    }

    @Override
    public ComentarioDTO obtenerPorId(Integer id) {
        return null;
    }


    @Override
    public ComentarioDTO actualizar(Integer id, ComentarioDTO comentarioDTO) {
        return null;
    }

    @Override
    public void eliminar(Integer id) {

    }


}