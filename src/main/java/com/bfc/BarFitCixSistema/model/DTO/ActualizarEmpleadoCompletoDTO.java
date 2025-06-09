package com.bfc.BarFitCixSistema.model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * Esta clase sirve para actualizar un empleado pero con todos sus datos
 * Por eso tiene validaciones
 * @author Tegafadax
 * @version 1.0
 * @since 2025-06-09
 */
@Data
public class ActualizarEmpleadoCompletoDTO {

    @NotBlank(message = "El nombre del empleado no puede estar vacío")
    private String nom_empleado;

    @NotBlank(message = "El correo corporativo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String ema_corporativo;

    private String contrasena; // Puede venir vacía si no desea cambiarse

    @NotBlank(message = "El rol es obligatorio")
    private String rol; // Ejemplo: "ADMIN" o "EMPLEADO"

    private Boolean activo; // true o false

    private LocalDateTime fec_salida; // Puede ser null si aún está activo
}
