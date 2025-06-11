package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.TipoCantidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCantidadDAO extends JpaRepository<TipoCantidad, Integer> {
    // Métodos personalizados si los necesitas
}

