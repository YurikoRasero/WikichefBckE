package com.yuri.WikichefBckE.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionDTO {

        private Integer id;
        private int puntuacion;
        private int usuarioId;
        private int recetaId;
    }



