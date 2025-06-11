package com.bfc.BarFitCixSistema.model.service;

import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.ActualizarEmpleadoCompletoDTO;
import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.CrearEmpleadoDTO;
import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.GETEmpleadoDTO;

import java.util.List;

public interface EmpleadoService {
    GETEmpleadoDTO crearEmpleado(CrearEmpleadoDTO dto);
    GETEmpleadoDTO actualizarEmpleado(Integer id, ActualizarEmpleadoCompletoDTO dto);
    void eliminarEmpleado(Integer id);
    List<GETEmpleadoDTO> listarTodos();
    GETEmpleadoDTO obtenerPorId(Integer id);
}
