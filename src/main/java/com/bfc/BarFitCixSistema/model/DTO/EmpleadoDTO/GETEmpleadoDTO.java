package com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO;

import lombok.Data;

/**
 * DTO para mostrar información pública del empleado en el frontend.
 */
@Data
public class GETEmpleadoDTO {
    private Integer id;
    private String nombreUsuario;
    private String correoElectronico;
    private String rol;
    private String estado; // ACTIVO o INACTIVO
}