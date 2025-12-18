package com.yuri.WikichefBckE.Service;

import com.yuri.WikichefBckE.dto.RecetaDTO;
import com.yuri.WikichefBckE.modelo.Receta;
import com.yuri.WikichefBckE.modelo.User;
import com.yuri.WikichefBckE.repository.RecetaRepository;
import com.yuri.WikichefBckE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecetaService {

    private static final RecetaRepository repo;
    private final UserRepository userRepo;

    public static void eliminar(Integer id) {
    }

    public Receta crear(String username, Receta receta) {
        User u = (User) userRepo.findByUsername(username).orElseThrow();
        receta.setAutor(u);
        return repo.save(receta);
    }

    public static Receta actualizar(int id, String username, Receta nueva) {
        Receta r = repo.findById(id).orElseThrow();

        if (!r.getAutor().getUsername().equals(username))
            throw new RuntimeException("No autorizado");

        r.setTitulo(nueva.getTitulo());
        r.setDescripcion(nueva.getDescripcion());
        r.setIngredientes(nueva.getIngredientes());
        r.setEtiquetas(nueva.getEtiquetas());

        return repo.save(r);
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
            return repo.searchByIngredient(ing, pageable);

        return repo.findAll(pageable);
    }
    public Receta get(int id) {
        return repo.findById(id).orElseThrow();
    }

    public static @Nullable List<RecetaDTO> listar() {
            return null;
    }

    public static @Nullable RecetaDTO obtenerPorId(Integer id) {
        return null;
    }

    public RecetaDTO crear(RecetaDTO recetaDTO) {
        return null;
    }
}