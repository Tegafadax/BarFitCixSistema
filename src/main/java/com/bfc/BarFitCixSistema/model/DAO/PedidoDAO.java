package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Pedido.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface PedidoDAO extends JpaRepository<Pedido, Integer> {

    /**
     * Busca todos los pedidos y los ordena por estado (PENDIENTE primero) y
     * luego por fecha de creación descendente.
     *
     * @return Una lista de pedidos ordenados para el historial.
     */
    @Query("SELECT p FROM Pedido p ORDER BY p.estado ASC, p.fechaCreacion DESC")
    List<Pedido> findAllOrderedByStatusAndDate();

    /**
     * Busca un pedido por su ID y carga ansiosamente (eagerly) sus detalles (subtotales)
     * y la información del empleado asociado.
     *
     * @param id El ID del pedido a buscar.
     * @return Un Optional que contiene el Pedido con sus detalles, si se encuentra.
     */
    @Query("SELECT p FROM Pedido p " +
            "LEFT JOIN FETCH p.detalles d " +
            "LEFT JOIN FETCH p.empleado e " +
            "LEFT JOIN FETCH d.producto " +
            "WHERE p.idPedido = :id")
    Optional<Pedido> findByIdWithDetails(@Param("id") Integer id);

    /**
     * Busca todos los pedidos atendidos por un empleado específico.
     *
     * @param idEmpleado El ID del empleado.
     * @return Una lista de pedidos asociados a ese empleado.
     */
    @Query("SELECT p FROM Pedido p WHERE p.empleado.id_empleado = :idEmpleado")
    List<Pedido> findByEmpleadoId(@Param("idEmpleado") Integer idEmpleado);
}