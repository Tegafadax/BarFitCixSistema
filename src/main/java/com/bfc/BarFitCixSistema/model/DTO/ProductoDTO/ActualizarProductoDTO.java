// ActualizarProductoDTO.java
package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class ActualizarProductoDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser positivo")
    private Integer idProducto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nomProducto;

    @Valid
    private List<CrearProductoDTO.InsumoDetalleDTO> insumos;
}
