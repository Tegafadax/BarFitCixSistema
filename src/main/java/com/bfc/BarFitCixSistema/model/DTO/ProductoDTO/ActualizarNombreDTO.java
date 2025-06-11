package com.bfc.BarFitCixSistema.model.DTO.ProductoDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import javax.validation.constraints.Size;

// DTO para actualizar solo el nombre
@Data
public class ActualizarNombreDTO {
    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser positivo")
    private Integer idProducto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nomProducto;
}
