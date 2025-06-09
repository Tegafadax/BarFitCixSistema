package com.bfc.BarFitCixSistema.model.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Esta clase DTO solo sirve para mostrar Datos No comprometidos del empleado en el Fronted mediante la API
 * @author Tegafadax
 * @version 1.0
 * @since 2025-06-09
 */
@Data
public class GETEmpleadoDTO {
    private Integer id_empleado;
    private String nom_empleado;
    private String ema_corporativo;
    private LocalDateTime fec_ingreso;
    private LocalDateTime fec_salida;
    private String rol; // Nombre del rol, por ejemplo "ADMIN"
}
