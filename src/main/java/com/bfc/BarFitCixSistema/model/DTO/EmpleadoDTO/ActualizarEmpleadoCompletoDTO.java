package com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar un empleado con todos sus datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEmpleadoCompletoDTO {
    private String nombreUsuario;
    private String correoElectronico;
    private String contrasena;
    private String rol;
    private Boolean activo;
}