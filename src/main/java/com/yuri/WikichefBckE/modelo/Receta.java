package com.yuri.WikichefBckE.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "_receta")
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String descripcion;

    /**
     * JSON string con ingredientes
     * [{"nombre":"Tomate","cantidad":"2u"}]
     */
    @Column(columnDefinition="TEXT")
    private String ingredientes;



    @ManyToOne
    @JoinColumn(name = "autor_id")
    private User autor;
    @ElementCollection
    @CollectionTable(name = "_receta_etiquetas", joinColumns = @JoinColumn(name = "_receta_id"))
    @Column(name = "etiquetas")
    private List<String> etiquetas;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
