// ProductoInsumo.java - CORREGIDO
package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "producto_insumo")
public class ProductoInsumo implements Serializable {

    @EmbeddedId
    private ProductoInsumoPK id;

    @Column(name = "cant_por_insumo", nullable = false)
    private Float cantPorInsumo;

    // IMPORTANTE: Cambiar la configuración de las relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", insertable = false, updatable = false)
    private Insumo insumo;

    @ManyToOne(fetch = FetchType.EAGER) // Cambiar a EAGER para TipoCantidad
    @JoinColumn(name = "id_tipo_cantidad", nullable = false)
    private TipoCantidad tipoCantidad;

    // Método helper para crear la clave primaria
    public void setProductoInsumo(Producto producto, Insumo insumo) {
        this.producto = producto;
        this.insumo = insumo;
        this.id = new ProductoInsumoPK(producto.getIdProducto(), insumo.getIdInsumo());
    }

    // Override toString para evitar referencias circulares en logs
    @Override
    public String toString() {
        return "ProductoInsumo{" +
                "id=" + id +
                ", cantPorInsumo=" + cantPorInsumo +
                ", tipoCantidad=" + (tipoCantidad != null ? tipoCantidad.getNomCantidad() : "null") +
                '}';
    }
}