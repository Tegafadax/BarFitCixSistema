package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import lombok.Data;

import java.math.BigDecimal;

// DTO para cambiar precio (POST) - NUEVO
@Data
public class CambiarPrecioDTO {
    private Integer idProducto;
    private BigDecimal nuevoPrecio;
    private Integer idEmpleado;
    private String motivo; // opcional
}