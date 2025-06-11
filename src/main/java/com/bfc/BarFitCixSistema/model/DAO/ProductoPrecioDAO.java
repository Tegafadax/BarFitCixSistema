package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.ProductoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoPrecioDAO extends JpaRepository<ProductoPrecio, Integer> {

    // Obtener el precio activo de un producto
    @Query("SELECT pp FROM ProductoPrecio pp WHERE pp.producto.idProducto = :idProducto AND pp.activo = true AND pp.fechaFin IS NULL")
    Optional<ProductoPrecio> findPrecioActivoByProducto(@Param("idProducto") Integer idProducto);

    // Obtener historial de precios de un producto
    @Query("SELECT pp FROM ProductoPrecio pp WHERE pp.producto.idProducto = :idProducto ORDER BY pp.fechaInicio DESC")
    List<ProductoPrecio> findHistorialByProducto(@Param("idProducto") Integer idProducto);

    // Cerrar precio anterior (marcar fecha_fin y activo = false)
    @Modifying
    @Transactional
    @Query("UPDATE ProductoPrecio pp SET pp.fechaFin = :fechaFin, pp.activo = false WHERE pp.producto.idProducto = :idProducto AND pp.activo = true AND pp.fechaFin IS NULL")
    void cerrarPrecioAnterior(@Param("idProducto") Integer idProducto, @Param("fechaFin") LocalDateTime fechaFin);
}