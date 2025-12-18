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
        private UserDTO usuario;
    }

