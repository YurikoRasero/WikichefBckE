package com.yuri.WikichefBckE.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecetaDTO {

        private Integer id;
        private String titulo;
        private String descripcion;
        private String ingredientes;  // JSON string
        private UserDTO autor;
        private List<String> etiquetas;
    }
