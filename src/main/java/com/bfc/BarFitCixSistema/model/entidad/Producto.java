// ACTUALIZACIÓN REQUERIDA EN Producto.java
package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "producto")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nom_producto", length = 100)
    private String nomProducto;

    // CAMBIO IMPORTANTE: Configuración de cascada mejorada
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductoPrecio> precios = new ArrayList<>();

    // CAMBIO IMPORTANTE: Configuración de cascada mejorada
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductoInsumo> insumos = new ArrayList<>();

    // Método helper mejorado para obtener el precio actual
    public ProductoPrecio getPrecioActual() {
        if (precios == null || precios.isEmpty()) {
            return null;
        }

        return precios.stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()) && p.getFechaFin() == null)
                .findFirst()
                .orElse(null);
    }

    // Métodos helper para gestionar insumos
    public void addInsumo(ProductoInsumo productoInsumo) {
        if (insumos == null) {
            insumos = new ArrayList<>();
        }
        insumos.add(productoInsumo);
        productoInsumo.setProducto(this);
    }

    public void removeInsumo(ProductoInsumo productoInsumo) {
        if (insumos != null) {
            insumos.remove(productoInsumo);
            productoInsumo.setProducto(null);
        }
    }

    // Métodos helper para gestionar precios
    public void addPrecio(ProductoPrecio precio) {
        if (precios == null) {
            precios = new ArrayList<>();
        }
        precios.add(precio);
        precio.setProducto(this);
    }

    // Override toString para evitar referencias circulares
    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", nomProducto='" + nomProducto + '\'' +
                ", cantidadInsumos=" + (insumos != null ? insumos.size() : 0) +
                ", cantidadPrecios=" + (precios != null ? precios.size() : 0) +
                '}';
    }
}