package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.Rol;
import com.bfc.BarFitCixSistema.model.entidad.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolDAO extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombre(RolNombre nombre);
}