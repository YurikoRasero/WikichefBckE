package com.yuri.WikichefBckE.repository;

import com.yuri.WikichefBckE.modelo.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    Optional<Calificacion> findByUsuarioIdAndRecetaId(int u, int r);
    List<Calificacion> findByRecetaId(int recetaId);

}