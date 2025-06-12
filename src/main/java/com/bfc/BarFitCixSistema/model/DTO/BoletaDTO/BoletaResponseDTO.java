package com.bfc.BarFitCixSistema.model.DTO.BoletaDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para devolver la información de una boleta creada.
 */
@Data
public class BoletaResponseDTO {
    private Integer idBoleta;
    private LocalDateTime fechaBoleta;
    private LocalDateTime fechaPago;
    private String nomCliente;
    private Integer dniCliente;
    private BigDecimal montoPago;
    private String metodoPago;
    private Integer idPedido;
    private String nombreEmpleado;
}