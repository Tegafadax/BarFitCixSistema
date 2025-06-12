package com.bfc.BarFitCixSistema.model.DTO.PedidoDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para enviar la información completa de un pedido al frontend.
 * Ideal para la vista de "Resumen de Pedido" o para cargar un pedido existente.
 */
@Data
public class PedidoResponseDTO {
    private Integer idPedido;
    private int mesa;
    private String sala;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String nombreEmpleado;
    private BigDecimal totalPedido;
    private List<SubtotalResponseDTO> detalles;

    /**
     * DTO anidado para representar cada producto dentro de la respuesta del pedido.
     */
    @Data
    public static class SubtotalResponseDTO {
        private Integer idProducto;
        private String nombreProducto;
        private int cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
        private String comentario;
    }
}