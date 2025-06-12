package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Representa la tabla de unión "subtotal", que contiene los detalles de cada pedido.
 * Utiliza @EmbeddedId para la clave compuesta.
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

    @Column(name = "comentario", length = 255)
    private String comentario;

    // Relación con Pedido.
    // @MapsId se usa para indicar que el campo "idPedido" de la clave compuesta (id)
    // se mapea a esta relación.
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPedido")
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    // Relación con Producto.
    // @MapsId se usa para indicar que el campo "idProducto" de la clave compuesta (id)
    // se mapea a esta relación.
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto_final")
    private Producto producto;
}