package com.bfc.BarFitCixSistema.model.DTO.InsumoDTO;

import lombok.Data;

@Data
public class GETInsumoDTO {
    private Integer idInsumo;
    private String nomInsumo;
    private Integer idTipoCantidad;
    private String nomTipoCantidad;
}