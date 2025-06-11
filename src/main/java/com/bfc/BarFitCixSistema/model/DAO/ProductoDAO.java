package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoDAO extends JpaRepository<Producto, Integer> {

    // Buscar productos por nombre (case insensitive) - MANTENER
    List<Producto> findByNomProductoContainingIgnoreCase(String nombre);

    // Query para obtener productos con precios actuales e insumos
    @Query("SELECT DISTINCT p FROM Producto p " +
            "LEFT JOIN FETCH p.precios pp " +
            "LEFT JOIN FETCH p.insumos pi " +
            "LEFT JOIN FETCH pi.insumo " +
            "LEFT JOIN FETCH pi.tipoCantidad " +
            "WHERE pp.activo = true AND pp.fechaFin IS NULL")
    List<Producto> findAllWithPreciosActivosAndInsumos();

    // Obtener un producto específico con precio actual e insumos
    @Query("SELECT p FROM Producto p " +
            "LEFT JOIN FETCH p.precios pp " +
            "LEFT JOIN FETCH p.insumos pi " +
            "LEFT JOIN FETCH pi.insumo " +
            "LEFT JOIN FETCH pi.tipoCantidad " +
            "WHERE p.idProducto = :id AND (pp.activo = true AND pp.fechaFin IS NULL OR pp IS NULL)")
    Producto findByIdWithPrecioActivoAndInsumos(@Param("id") Integer id);

    // Obtener productos con precio en un rango específico
    @Query("SELECT DISTINCT p FROM Producto p " +
            "JOIN p.precios pp " +
            "WHERE pp.activo = true AND pp.fechaFin IS NULL " +
            "AND pp.precio BETWEEN :precioMin AND :precioMax")
    List<Producto> findByRangoPrecio(@Param("precioMin") java.math.BigDecimal precioMin,
                                     @Param("precioMax") java.math.BigDecimal precioMax);
}