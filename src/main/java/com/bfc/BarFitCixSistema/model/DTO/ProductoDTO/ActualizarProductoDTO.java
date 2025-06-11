package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

// DTO para actualizar un producto (PUT) - SIN CAMBIAR PRECIO
@Data
public class ActualizarProductoDTO {
    private Integer idProducto;
    private String nomProducto;
    private List<CrearProductoDTO.InsumoDetalleDTO> insumos;
}
