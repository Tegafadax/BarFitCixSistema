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
 * DTO para recibir la solicitud de creación de boletas (pago único o fraccionado).
 */
@Data
public class CrearBoletaRequestDTO {

    @NotNull
    @Positive
    private Integer idPedido;

    @NotNull
    @Positive
    private Integer idEmpleado;

    @NotEmpty
    @Valid
    private List<PagoDetalleDTO> pagos;

    @Data
    public static class PagoDetalleDTO {
        @NotNull
        @Size(min = 3, max = 255)
        private String nomCliente;

        @NotNull
        private Integer dniCliente;

        @NotNull
        @Positive
        private BigDecimal montoPago;

        @NotNull
        private String metodoPago;
    }
}