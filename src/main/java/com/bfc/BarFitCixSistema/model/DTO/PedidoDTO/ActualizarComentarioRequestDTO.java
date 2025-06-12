package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para actualizar el comentario de un producto en un pedido.
 */
@Data
public class ActualizarComentarioRequestDTO {

    @NotNull
    @Positive
    private Integer idPedido;

    @NotNull
    @Positive
    private Integer idProducto;

    @Size(max = 255)
    private String comentario;
}