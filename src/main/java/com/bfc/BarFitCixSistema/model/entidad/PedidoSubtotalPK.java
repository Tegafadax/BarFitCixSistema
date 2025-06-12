package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la clave primaria compuesta para la entidad Subtotal.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PedidoSubtotalPK implements Serializable {

    @Column(name = "id_pedido")
    private Integer idPedido;

    @Column(name = "id_producto_final")
    private Integer idProducto;

    // Es crucial implementar equals y hashCode para las claves compuestas.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoSubtotalPK that = (PedidoSubtotalPK) o;
        return Objects.equals(idPedido, that.idPedido) &&
                Objects.equals(idProducto, that.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido, idProducto);
    }
}