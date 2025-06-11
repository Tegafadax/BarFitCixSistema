package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import lombok.Data;

import java.util.List;

// DTO para crear un nuevo producto (POST)
@Data
public class CrearProductoDTO {
    private String nomProducto;
    private Float precioDeProductos;
    private List<InsumoDetalleDTO> insumos;

    @Data
    public static class InsumoDetalleDTO {
        private Integer idInsumo;
        private Float cantPorInsumo;
        private Integer idTipoCantidad;
    }
}
