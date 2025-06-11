package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

// DTO para obtener/listar productos (GET)
@Data
public class GETProductoDTO {
    private Integer idProducto;
    private String nomProducto;

    // Precio actual
    private BigDecimal precioActual;
    private LocalDateTime fechaPrecioActual;
    private String empleadoModificoPrecio;

    // Insumos (mantener igual)
    private List<ProductoInsumoDTO> insumos;

    @Data
    public static class ProductoInsumoDTO {
        private Integer idInsumo;
        private String nomInsumo;
        private Float cantPorInsumo;
        private String nomTipoCantidad;
    }
}