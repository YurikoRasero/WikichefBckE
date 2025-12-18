package com.yuri.WikichefBckE.repository;

import com.yuri.WikichefBckE.modelo.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
   @Query("SELECT r FROM Receta r WHERE LOWER(r.titulo) LIKE LOWER(CONCAT('%',:q,'%'))")
   List<Receta> search(@Param("q") String q);

    Page<Receta> findByTituloContainingIgnoreCase(String q, Pageable pageable);

    Page<Receta> searchByIngredient(String ing, Pageable pageable);
}