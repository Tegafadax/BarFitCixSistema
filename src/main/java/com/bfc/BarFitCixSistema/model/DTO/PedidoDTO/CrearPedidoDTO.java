package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

/**
 * DTO para recibir la solicitud de creación de un nuevo pedido desde el frontend.
 * Corresponde a la interfaz donde se seleccionan los platos y se guardan.
 */
@Data
public class CrearPedidoDTO {

    @NotNull(message = "El número de mesa es obligatorio.")
    @Positive(message = "El número de mesa debe ser positivo.")
    private Integer mesa;

    private String sala;

    @NotNull(message = "El ID del empleado que atiende es obligatorio.")
    @Positive(message = "El ID del empleado debe ser válido.")
    private Integer idEmpleado;

    @NotEmpty(message = "El pedido debe contener al menos un producto.")
    @Valid // Esto asegura que los objetos dentro de la lista también se validen.
    private List<PedidoDetalleDTO> detalles;

    /**
     * DTO anidado que representa cada línea de producto en el pedido.
     */
    @Data
    public static class PedidoDetalleDTO {

        @NotNull(message = "El ID del producto es obligatorio.")
        @Positive
        private Integer idProducto;

        @NotNull(message = "La cantidad del producto es obligatoria.")
        @Positive(message = "La cantidad debe ser mayor a cero.")
        private Integer cantidad;

        // El comentario es opcional
        private String comentario;
    }
}