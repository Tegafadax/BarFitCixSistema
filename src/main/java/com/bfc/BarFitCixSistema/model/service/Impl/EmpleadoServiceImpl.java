package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DAO.EmpleadoDAO;
import com.bfc.BarFitCixSistema.model.DAO.RolDAO;
import com.bfc.BarFitCixSistema.model.DTO.*;
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

        Empleado empleado = new Empleado(
                dto.getNom_empleado(),
                dto.getEma_corporativo(),
                dto.getContrasena(),

                rol
        );

        empleado = empleadoDAO.save(empleado);
        return convertirAGetDTO(empleado);
    }

    @Override
    public GETEmpleadoDTO actualizarEmpleado(Integer id, ActualizarEmpleadoCompletoDTO dto) {
        Empleado empleado = empleadoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con id: " + id));

        empleado.setNom_empleado(dto.getNom_empleado());
        empleado.setEma_corporativo(dto.getEma_corporativo());
        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            empleado.setContrasena(dto.getContrasena());
        }
        Rol rol = rolDAO.findByNombre(RolNombre.valueOf(dto.getRol()))
                .orElseThrow(() -> new IllegalArgumentException("Rol inválido"));
        empleado.setRol(rol);

        empleado.setActivo(dto.getActivo() != null ? dto.getActivo() : empleado.getActivo());
        empleado.setFec_salida(dto.getFec_salida());

        empleado = empleadoDAO.save(empleado);
        return convertirAGetDTO(empleado);
    }

    @Override
    public void eliminarEmpleado(Integer id) {
        Empleado empleado = empleadoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con id: " + id));

        empleado.setActivo(false);
        empleado.setFec_salida(java.time.LocalDateTime.now());
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
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con id: " + id));
        return convertirAGetDTO(empleado);
    }

    private GETEmpleadoDTO convertirAGetDTO(Empleado empleado) {
        GETEmpleadoDTO dto = new GETEmpleadoDTO();
        dto.setId_empleado(empleado.getId_empleado());
        dto.setNom_empleado(empleado.getNom_empleado());
        dto.setEma_corporativo(empleado.getEma_corporativo());
        dto.setFec_ingreso(empleado.getFec_ingreso());
        dto.setFec_salida(empleado.getFec_salida());
        dto.setRol(empleado.getRol().getNombre().name());
        return dto;
    }
}
