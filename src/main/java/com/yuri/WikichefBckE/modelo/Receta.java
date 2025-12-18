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
    @GeneratedValue
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
    private User autor;
    @ElementCollection
    private List<String> etiquetas;
    private LocalDateTime createdAt;
}
