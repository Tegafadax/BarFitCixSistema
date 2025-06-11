package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

// DTO para actualizar un producto (PUT)
@Data
public class ActualizarProductoDTO {
    private Integer idProducto;
    private String nomProducto;
    private Float precioDeProductos;
    private LocalDate fecFin;
    private List<CrearProductoDTO.InsumoDetalleDTO> insumos;
}
