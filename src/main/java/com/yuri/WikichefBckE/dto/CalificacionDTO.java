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
        /**
         * ID del usuario que hizo la calificación.
         * IGNORADO en requests - se establece automáticamente desde el usuario autenticado.
         * Incluido en responses para mostrar información del usuario.
         */
        private int usuarioId;
        /**
         * ID de la receta calificada.
         * IGNORADO en requests - se obtiene del path parameter {recetaId}.
         * Incluido en responses para mostrar información de la receta.
         */
        private int recetaId;
    }



