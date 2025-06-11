package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para mostrar historial de precios (GET) - NUEVO
@Data
public class HistorialPrecioDTO {
    private Integer idPrecio;
    private BigDecimal precio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean activo;
    private String empleadoModifico;
    private String motivo;
}
