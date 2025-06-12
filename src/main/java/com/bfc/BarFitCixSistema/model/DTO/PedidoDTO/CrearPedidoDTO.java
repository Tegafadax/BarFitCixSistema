package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

/**
 * DTO para recibir la solicitud de creación de un nuevo pedido.
 */
@Data
public class CrearPedidoDTO {

    @NotNull(message = "El número de mesa es obligatorio.")
    @Positive
    private Integer mesa;

    private String sala;

    @NotNull(message = "El ID del empleado es obligatorio.")
    @Positive
    private Integer idEmpleado;

    @NotEmpty(message = "El pedido debe contener al menos un producto.")
    @Valid
    private List<PedidoDetalleDTO> detalles;

    @Data
    public static class PedidoDetalleDTO {
        @NotNull
        @Positive
        private Integer idProducto;

        @NotNull
        @Positive
        private Integer cantidad;

        private String comentario;
    }
}