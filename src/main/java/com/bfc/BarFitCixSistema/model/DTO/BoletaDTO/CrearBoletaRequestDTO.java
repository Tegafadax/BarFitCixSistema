package com.bfc.BarFitCixSistema.model.DTO.BoletaDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para recibir la solicitud de creación de una o más boletas para un pedido.
 * Soporta tanto pagos únicos como pagos fraccionados a través de la lista de pagos.
 */
@Data
public class CrearBoletaRequestDTO {

    @NotNull(message = "El ID del pedido es obligatorio.")
    @Positive
    private Integer idPedido;

    @NotNull(message = "El ID del empleado es obligatorio.")
    @Positive
    private Integer idEmpleado;

    @NotEmpty(message = "Debe haber al menos un detalle de pago.")
    @Valid
    private List<PagoDetalleDTO> pagos;

    @Data
    public static class PagoDetalleDTO {

        @NotNull(message = "El nombre del cliente es obligatorio.")
        @Size(min = 3, max = 255)
        private String nomCliente;

        @NotNull(message = "El DNI del cliente es obligatorio.")
        private Integer dniCliente;

        @NotNull(message = "El monto del pago es obligatorio.")
        @Positive(message = "El monto del pago debe ser positivo.")
        private BigDecimal montoPago;

        @NotNull(message = "El método de pago es obligatorio.")
        private String metodoPago; // Ej: "Efectivo", "Yape", "Plin", "Tarjeta"
    }
}