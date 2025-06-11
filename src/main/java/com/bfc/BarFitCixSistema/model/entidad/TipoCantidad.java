package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tipo_cantidad")
@Data
public class TipoCantidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cantidad")
    private Integer idTipoCantidad;

    @Column(name = "nom_cantidad", nullable = false, length = 20)
    private String nomCantidad;
}
