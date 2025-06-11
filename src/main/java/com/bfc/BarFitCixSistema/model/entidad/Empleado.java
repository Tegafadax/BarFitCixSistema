package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * Esta clase Representa la Tabla de la Base de Datos Empleado
 *
 * @author Tegafadax
 * @version 1.0
 * @since 2025-06-09
 */
@Entity
@Table(name = "empleado")
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_empleado;

    @Column(name = "nom_empleado", nullable = false)
    private String nom_empleado;

    @Column(name = "ema_corporativo", nullable = false, unique = true)
    private String ema_corporativo;

    // Esto sirve para que cuando creemos un empleado la fecha salga del sistema
    @Column(name = "fec_ingreso", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fec_ingreso;

    //Esto sirve para que cuando creamos una fecha salida se jale del sistema al borrar un empleado
    @Column(name = "fec_salida", nullable = true)
    private LocalDateTime fec_salida;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    public Integer getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(Integer id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNom_empleado() {
        return nom_empleado;
    }

    public void setNom_empleado(String nom_empleado) {
        this.nom_empleado = nom_empleado;
    }

    public String getEma_corporativo() {
        return ema_corporativo;
    }

    public void setEma_corporativo(String ema_corporativo) {
        this.ema_corporativo = ema_corporativo;
    }

    public LocalDateTime getFec_ingreso() {
        return fec_ingreso;
    }

    public void setFec_ingreso(LocalDateTime fec_ingreso) {
        this.fec_ingreso = fec_ingreso;
    }

    public LocalDateTime getFec_salida() {
        return fec_salida;
    }

    public void setFec_salida(LocalDateTime fec_salida) {
        this.fec_salida = fec_salida;
    }

    public Boolean getActivo() {
        return activo;
    }
    // Getter para isActivo()
    public boolean isActivo() {
        return activo != null && activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Constructor para creación de empleados
    public Empleado(String nom_empleado, String ema_corporativo, String contrasena, Rol rol) {
        this.nom_empleado = nom_empleado;
        this.ema_corporativo = ema_corporativo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.activo = true;
        this.fec_ingreso = LocalDateTime.now();
    }
}