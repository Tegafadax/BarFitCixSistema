package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.Empleado;
import com.bfc.BarFitCixSistema.model.entidad.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoDAO extends JpaRepository<Empleado, Integer> {

    // Listar empleados activos
    List<Empleado> findByActivoTrue();

    // Listar empleados por rol
    List<Empleado> findByRolNombre(RolNombre nombre); // nombre = ADMIN o EMPLEADO

    // Buscar empleado por ID
    Optional<Empleado> findById(Integer id);
}
