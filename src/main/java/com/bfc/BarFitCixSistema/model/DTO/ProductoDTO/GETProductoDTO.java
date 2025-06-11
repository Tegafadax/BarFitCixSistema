package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

// DTO para obtener/listar productos (GET)
@Data
public class GETProductoDTO {
    private Integer idProducto;
    private String nomProducto;
    private Float precioDeProductos;
    private LocalDateTime fecInicio;
    private LocalDate fecFin;
    private List<ProductoInsumoDTO> insumos;

    @Data
    public static class ProductoInsumoDTO {
        private Integer idInsumo;
        private String nomInsumo;
        private Float cantPorInsumo;
        private String nomTipoCantidad;
    }
}