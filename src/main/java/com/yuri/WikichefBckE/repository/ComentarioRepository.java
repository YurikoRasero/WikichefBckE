package com.yuri.WikichefBckE.repository;


import com.yuri.WikichefBckE.modelo.Comentario;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository {
    List<Comentario> findByRecetaIdOrderByIdDesc(int recetaId);

    Comentario save(Comentario c);
}