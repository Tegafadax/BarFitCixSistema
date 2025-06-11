package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProductoInsumoPK implements Serializable {

    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "id_insumo")
    private Integer idInsumo;
}