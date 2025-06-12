package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO para manejar la solicitud de actualizar la cantidad de un producto
 * dentro de un pedido existente.
 */
@Data
public class ActualizarCantidadRequestDTO {

    @NotNull(message = "El ID del pedido es obligatorio.")
    @Positive
    private Integer idPedido;

    @NotNull(message = "El ID del producto es obligatorio.")
    @Positive
    private Integer idProducto;

    @NotNull(message = "La nueva cantidad es obligatoria.")
    @Positive(message = "La cantidad debe ser mayor a cero.")
    private Integer nuevaCantidad;
}