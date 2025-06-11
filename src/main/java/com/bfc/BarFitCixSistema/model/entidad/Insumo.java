package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "insumo")
@Data
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Integer idInsumo;

    @Column(name = "nom_insumo", nullable = false, length = 100)
    private String nomInsumo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_cantidad", nullable = false)
    private TipoCantidad tipoCantidad;
}
