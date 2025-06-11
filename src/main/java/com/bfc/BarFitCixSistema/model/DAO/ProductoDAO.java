package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoDAO extends JpaRepository<Producto, Integer> {

    // ===================== MÉTODOS ORIGINALES MODIFICADOS =====================

    // Buscar productos por nombre - AHORA SOLO CON PRECIO ACTIVO
    @Query("SELECT DISTINCT p FROM Producto p " +
            "JOIN p.precios pp " +
            "WHERE UPPER(p.nomProducto) LIKE UPPER(CONCAT('%', :nombre, '%')) " +
            "AND pp.activo = true AND pp.fechaFin IS NULL")
    List<Producto> findByNomProductoContainingIgnoreCase(@Param("nombre") String nombre);

    // Query básica sin FETCH JOIN múltiple - MANTENER IGUAL
    @Query("SELECT p FROM Producto p WHERE p.idProducto = :id")
    Producto findByIdBasic(@Param("id") Integer id);

    // Query solo con insumos - MANTENER IGUAL
    @Query("SELECT DISTINCT p FROM Producto p " +
            "LEFT JOIN FETCH p.insumos pi " +
            "LEFT JOIN FETCH pi.insumo " +
            "LEFT JOIN FETCH pi.tipoCantidad " +
            "WHERE p.idProducto = :id")
    Producto findByIdWithInsumos(@Param("id") Integer id);

    // Para listar todos - AHORA SOLO PRODUCTOS CON PRECIO ACTIVO
    @Query("SELECT DISTINCT p FROM Producto p " +
            "JOIN p.precios pp " +
            "WHERE pp.activo = true AND pp.fechaFin IS NULL")
    List<Producto> findAllBasic();

    // Para obtener productos con precio en un rango específico - MANTENER IGUAL (ya filtraba)
    @Query("SELECT DISTINCT p FROM Producto p " +
            "JOIN p.precios pp " +
            "WHERE pp.activo = true AND pp.fechaFin IS NULL " +
            "AND pp.precio BETWEEN :precioMin AND :precioMax")
    List<Producto> findByRangoPrecio(@Param("precioMin") java.math.BigDecimal precioMin,
                                     @Param("precioMax") java.math.BigDecimal precioMax);

    // Para verificar existencia sin cargar entidad completa - MANTENER IGUAL
    boolean existsByNomProductoIgnoreCase(String nomProducto);

    // ===================== MÉTODOS NUEVOS AGREGADOS =====================

    // NUEVO: Para listar TODOS los productos (incluidos sin precio activo) - PARA ADMIN
    @Query("SELECT p FROM Producto p")
    List<Producto> findAllProductos();

    // NUEVO: Buscar productos por nombre SIN FILTRO de precio activo - PARA ADMIN
    @Query("SELECT DISTINCT p FROM Producto p " +
            "WHERE UPPER(p.nomProducto) LIKE UPPER(CONCAT('%', :nombre, '%'))")
    List<Producto> findByNomProductoContainingIgnoreCaseAdmin(@Param("nombre") String nombre);

    // NUEVO: Verificar si producto tiene precio activo
    @Query("SELECT COUNT(pp) > 0 FROM ProductoPrecio pp " +
            "WHERE pp.producto.idProducto = :idProducto " +
            "AND pp.activo = true AND pp.fechaFin IS NULL")
    boolean existsProductoWithPrecioActivo(@Param("idProducto") Integer idProducto);

    // NUEVO: Obtener solo productos con precio activo mayor a 0 (para disponibles)
    @Query("SELECT DISTINCT p FROM Producto p " +
            "JOIN p.precios pp " +
            "WHERE pp.activo = true AND pp.fechaFin IS NULL " +
            "AND pp.precio > 0")
    List<Producto> findProductosDisponibles();
}