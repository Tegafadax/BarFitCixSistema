package com.bfc.BarFitCixSistema.model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
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

    // CORRECCIÓN: Se añade el campo faltante.
    @Column(name = "fec_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "nom_cliente", nullable = false)
    private String nombreCliente;

    @Column(name = "dni_cliente", nullable = false)
    private int dniCliente;

    // NOTA: Usamos BigDecimal para montos de dinero por su precisión,
    // aunque la tabla use FLOAT. JPA/Hibernate manejará la conversión.
    @Column(name = "monto_pago", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPago;

    @Column(name = "met_pago", nullable = false)
    private String metodoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    // Muchas boletas pueden pertenecer a un mismo pedido.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;
}
