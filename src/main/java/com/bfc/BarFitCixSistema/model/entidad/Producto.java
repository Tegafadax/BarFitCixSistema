package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
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

    // Relación con los precios (historial)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductoPrecio> precios;

    // Relación con los insumos (recetas)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<ProductoInsumo> insumos;

    // Método helper para obtener el precio actual
    public ProductoPrecio getPrecioActual() {
        return precios != null ?
                precios.stream()
                        .filter(p -> p.getActivo() && p.getFechaFin() == null)
                        .findFirst()
                        .orElse(null) : null;
    }
}