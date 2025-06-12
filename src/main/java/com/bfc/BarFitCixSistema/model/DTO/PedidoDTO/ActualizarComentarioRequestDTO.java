package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para manejar la solicitud de actualizar el comentario de un producto
 * específico dentro de un pedido existente.
 */
@Data
public class ActualizarComentarioRequestDTO {

    @NotNull(message = "El ID del pedido es obligatorio.")
    @Positive
    private Integer idPedido;

    @NotNull(message = "El ID del producto es obligatorio.")
    @Positive
    private Integer idProducto;

    @Size(max = 255, message = "El comentario no puede exceder los 255 caracteres.")
    private String comentario; // Puede ser nulo o vacío para eliminar un comentario.
}