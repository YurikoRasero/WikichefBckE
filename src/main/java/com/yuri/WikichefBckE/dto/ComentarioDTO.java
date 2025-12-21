package com.yuri.WikichefBckE.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioDTO {

        private Integer id;
        private String texto;
        /**
         * Usuario que hizo el comentario.
         * IGNORADO en requests - se establece automáticamente desde el usuario autenticado.
         * Incluido en responses para mostrar información del usuario.
         */
        private UserDTO usuario;
    }

