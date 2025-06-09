package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase Representa la Tabla de la Base de Datos Rol
 *
 * @author Tegafadax
 * @version 1.0
 * @since 2025-06-09
 */
@Entity
@Table(name = "rol")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Rol {

    @Id
    private Integer id_rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "desc_rol", nullable = false, unique = true)
    private RolNombre nombre;

}