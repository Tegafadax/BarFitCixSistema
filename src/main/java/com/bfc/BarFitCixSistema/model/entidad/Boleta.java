package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Representa la tabla de Boletas en la base de datos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "boleta")
public class Boleta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleta")
    private Integer idBoleta;

    @Column(name = "fec_boleta", nullable = false)
    private LocalDateTime fechaBoleta;

    @Column(name = "nom_cliente", nullable = false)
    private String nombreCliente;

    @Column(name = "dni_cliente", nullable = false)
    private int dniCliente;

    @Column(name = "fec_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "monto_pago", nullable = false)
    private float montoPago;

    @Column(name = "met_pago", nullable = false)
    private String metodoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private Pedido pedido;
}