package com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo empleado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearEmpleadoDTO {
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String nombreUsuario;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String contrasena;

    @NotBlank(message = "El rol es obligatorio.")
    private String rol;
}