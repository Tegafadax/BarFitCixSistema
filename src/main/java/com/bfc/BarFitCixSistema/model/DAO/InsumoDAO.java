package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsumoDAO extends JpaRepository<Insumo, Integer> {

    // Buscar insumos por tipo de cantidad
    List<Insumo> findByTipoCantidadIdTipoCantidad(Integer idTipoCantidad);

    // Buscar insumo por nombre (case insensitive)
    List<Insumo> findByNomInsumoContainingIgnoreCase(String nombre);

    // Query personalizada para obtener insumos con información del tipo de cantidad
    @Query("SELECT i FROM Insumo i JOIN FETCH i.tipoCantidad")
    List<Insumo> findAllWithTipoCantidad();
}