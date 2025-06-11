package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoDAO extends JpaRepository<Producto, Integer> {

    // Buscar productos por nombre (case insensitive)
    List<Producto> findByNomProductoContainingIgnoreCase(String nombre);

    // Buscar productos activos (sin fecha fin)
    List<Producto> findByFecFinIsNull();

    // Buscar productos inactivos (con fecha fin)
    List<Producto> findByFecFinIsNotNull();

    // Query personalizada para obtener productos con sus insumos
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.insumos pi LEFT JOIN FETCH pi.insumo LEFT JOIN FETCH pi.tipoCantidad")
    List<Producto> findAllWithInsumos();

    // Obtener un producto específico con sus insumos
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.insumos pi LEFT JOIN FETCH pi.insumo LEFT JOIN FETCH pi.tipoCantidad WHERE p.idProducto = :id")
    Producto findByIdWithInsumos(@Param("id") Integer id);
}