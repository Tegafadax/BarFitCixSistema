package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DAO.EmpleadoDAO;
import com.bfc.BarFitCixSistema.model.DAO.RolDAO;
import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.ActualizarEmpleadoCompletoDTO;
import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.CrearEmpleadoDTO;
import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.GETEmpleadoDTO;
import com.bfc.BarFitCixSistema.model.entidad.Empleado;
import com.bfc.BarFitCixSistema.model.entidad.Rol;
import com.bfc.BarFitCixSistema.model.entidad.RolNombre;
import com.bfc.BarFitCixSistema.model.service.EmpleadoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoDAO empleadoDAO;
    private final RolDAO rolDAO;

    @Override
    public GETEmpleadoDTO crearEmpleado(CrearEmpleadoDTO dto) {
        Rol rol = rolDAO.findByNombre(RolNombre.valueOf(dto.getRol()))
                .orElseThrow(() -> new IllegalArgumentException("Rol inválido"));

        Empleado empleado = new Empleado();
        empleado.setNom_empleado(dto.getNombreUsuario());
        empleado.setEma_corporativo(dto.getCorreoElectronico());
        empleado.setContrasena(dto.getContrasena());
        empleado.setRol(rol);
        empleado.setFec_ingreso(LocalDateTime.now());
        empleado.setActivo(true); // Por defecto es activo al crear

        empleado = empleadoDAO.save(empleado);
        return convertirAGetDTO(empleado);
    }

    @Override
    public GETEmpleadoDTO actualizarEmpleado(Integer id, ActualizarEmpleadoCompletoDTO dto) {
        Empleado empleado = empleadoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

        if (dto.getNombreUsuario() != null) empleado.setNom_empleado(dto.getNombreUsuario());
        if (dto.getCorreoElectronico() != null) empleado.setEma_corporativo(dto.getCorreoElectronico());
        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            empleado.setContrasena(dto.getContrasena());
        }
        if (dto.getRol() != null) {
            Rol rol = rolDAO.findByNombre(RolNombre.valueOf(dto.getRol()))
                    .orElseThrow(() -> new IllegalArgumentException("Rol inválido"));
            empleado.setRol(rol);
        }
        if (dto.getActivo() != null) {
            empleado.setActivo(dto.getActivo());
            if (!dto.getActivo()) {
                empleado.setFec_salida(LocalDateTime.now()); // Marca fecha de salida si se desactiva
            }
        }

        empleado = empleadoDAO.save(empleado);
        return convertirAGetDTO(empleado);
    }

    @Override
    public void eliminarEmpleado(Integer id) {
        Empleado empleado = empleadoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));

        empleado.setActivo(false);
        empleado.setFec_salida(LocalDateTime.now());
        empleadoDAO.save(empleado);
    }

    @Override
    public List<GETEmpleadoDTO> listarTodos() {
        return empleadoDAO.findAll().stream()
                .map(this::convertirAGetDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GETEmpleadoDTO obtenerPorId(Integer id) {
        Empleado empleado = empleadoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));
        return convertirAGetDTO(empleado);
    }

    private GETEmpleadoDTO convertirAGetDTO(Empleado empleado) {
        GETEmpleadoDTO dto = new GETEmpleadoDTO();
        dto.setId(empleado.getId_empleado());
        dto.setNombreUsuario(empleado.getNom_empleado());
        dto.setCorreoElectronico(empleado.getEma_corporativo());
        dto.setRol(empleado.getRol().getNombre().name());
        dto.setEstado(empleado.isActivo() ? "ACTIVO" : "INACTIVO");
        return dto;
    }
}