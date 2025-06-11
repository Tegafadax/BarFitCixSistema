// ProductoInsumoPK.java - CORREGIDO
package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProductoInsumoPK implements Serializable {

    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "id_insumo")
    private Integer idInsumo;

    // CRÍTICO: Implementar equals y hashCode para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoInsumoPK that = (ProductoInsumoPK) o;
        return Objects.equals(idProducto, that.idProducto) &&
                Objects.equals(idInsumo, that.idInsumo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idInsumo);
    }

    @Override
    public String toString() {
        return "ProductoInsumoPK{" +
                "idProducto=" + idProducto +
                ", idInsumo=" + idInsumo +
                '}';
    }
}