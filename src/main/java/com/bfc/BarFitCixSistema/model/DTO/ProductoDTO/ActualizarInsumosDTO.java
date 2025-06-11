package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

// DTO para actualizar solo los insumos
@Data
public class ActualizarInsumosDTO {
    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser positivo")
    private Integer idProducto;

    @NotEmpty(message = "Debe especificar al menos un insumo")
    @Valid
    private List<CrearProductoDTO.InsumoDetalleDTO> insumos;
}
