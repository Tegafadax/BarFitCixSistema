package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO optimizado para la lista del historial de pedidos.
 */
@Data
public class PedidoHistorialDTO {
    private Integer idPedido;
    private String sala;
    private String nombreAtendido;
    private int mesa;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
}