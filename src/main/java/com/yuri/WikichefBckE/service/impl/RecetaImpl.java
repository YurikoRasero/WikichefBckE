package com.yuri.WikichefBckE.service.impl;

import com.yuri.WikichefBckE.modelo.Receta;
import com.yuri.WikichefBckE.service.RecetaService;
import com.yuri.WikichefBckE.dto.RecetaDTO;
import com.yuri.WikichefBckE.dto.UserDTO;
import com.yuri.WikichefBckE.repository.RecetaRepository;
import com.yuri.WikichefBckE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecetaImpl implements RecetaService {

    private final RecetaRepository repo;
    private final UserRepository userRepo;

    public void eliminar(Integer id) {
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


    public Receta crear(RecetaDTO recetaDTO) {
        return null;
    }

    @Override
    public List<RecetaDTO> listar() {
        return List.of();
    }

    @Override
    public RecetaDTO obtenerPorId(Integer id) {
        return null;
    }

    @Override
    public Receta actualizar(Integer id, RecetaDTO recetaDTO) {
        Receta r = repo.findById(id).orElseThrow();
        r.setTitulo(recetaDTO.getTitulo());
        r.setDescripcion(recetaDTO.getDescripcion());
        r.setIngredientes(recetaDTO.getIngredientes());
        r.setEtiquetas(recetaDTO.getEtiquetas());

        return repo.save(r);
    }



}