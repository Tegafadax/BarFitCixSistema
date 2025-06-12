package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Representa la tabla de unión "subtotal", que contiene los detalles de cada pedido.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "subtotal")
public class Subtotal implements Serializable {

    @EmbeddedId
    private PedidoSubtotalPK id;

    @Column(name = "cant_num_prod")
    private Integer cantidad;

    @Column(name = "comentario")
    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPedido") // Mapea la propiedad "idPedido" de la PK embebida
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto") // Mapea la propiedad "idProducto" de la PK embebida
    @JoinColumn(name = "id_producto_final")
    private Producto producto;
}