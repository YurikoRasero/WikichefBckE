package com.yuri.WikichefBckE.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogError {
    private LocalDateTime fecha;
    private String detalle;
    private String mensaje;
}
