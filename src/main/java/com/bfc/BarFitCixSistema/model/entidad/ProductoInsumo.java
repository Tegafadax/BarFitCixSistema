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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    @MapsId("idProducto")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", insertable = false, updatable = false)
    @MapsId("idInsumo")
    private Insumo insumo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_cantidad", nullable = false)
    private TipoCantidad tipoCantidad;
}