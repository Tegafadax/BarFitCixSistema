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

    @Column(name = "precio_de_productos", nullable = false)
    private Float precioDeProductos;

    @Column(name = "nom_producto", length = 100)
    private String nomProducto;

    @Column(name = "fec_inicio", nullable = false, insertable = true, updatable = false)
    @CreationTimestamp
    private LocalDateTime fecInicio;

    @Column(name = "fec_fin")
    private LocalDate fecFin;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<ProductoInsumo> insumos;
}