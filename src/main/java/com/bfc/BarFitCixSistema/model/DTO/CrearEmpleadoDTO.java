package com.bfc.BarFitCixSistema.model.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Esta clase sirve para crear un Nuevo Empleado Por eso solo necesita nombre, email, contraseña, rol
 * @author Tegafadax
 * @version 1.0
 * @since 2025-06-09
 */
@Data
public class CrearEmpleadoDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nom_empleado;

    @NotBlank(message = "El correo corporativo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String ema_corporativo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasena;

    @NotNull(message = "El rol no puede ser nulo")
    private String rol; // Ej: "ADMIN" o "EMPLEADO"

}
