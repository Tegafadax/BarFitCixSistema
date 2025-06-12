package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO optimizado para la lista que se muestra en la pantalla "Historial de Pedidos".
 * Contiene solo la información necesaria para la vista de tabla.
 */
@Data
public class PedidoHistorialDTO {
    private Integer idPedido;
    private String sala;
    private String nombreAtendido; // Nombre del empleado
    private int mesa;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado; // "PENDIENTE" o "FINALIZADO"
}