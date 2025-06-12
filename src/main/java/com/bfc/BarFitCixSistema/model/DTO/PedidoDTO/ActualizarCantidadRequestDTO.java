package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO para actualizar la cantidad de un producto en un pedido existente.
 */
@Data
public class ActualizarCantidadRequestDTO {

    @NotNull
    @Positive
    private Integer idPedido;

    @NotNull
    @Positive
    private Integer idProducto;

    @NotNull
    @Positive
    private Integer nuevaCantidad;
}