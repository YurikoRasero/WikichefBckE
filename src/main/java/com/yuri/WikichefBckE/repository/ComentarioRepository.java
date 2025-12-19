package com.yuri.WikichefBckE.repository;


import com.yuri.WikichefBckE.modelo.Comentario;
import com.yuri.WikichefBckE.modelo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario,Integer> {
    List<Comentario> findByRecetaIdOrderByIdDesc(int recetaId);
    List<Comentario> findByRecetaId(Integer recetaId);
    Comentario save(Comentario c);
}