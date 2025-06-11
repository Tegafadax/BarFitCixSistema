package com.bfc.BarFitCixSistema.controller;

import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.ActualizarEmpleadoCompletoDTO;
import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.CrearEmpleadoDTO;
import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.GETEmpleadoDTO;
import com.bfc.BarFitCixSistema.model.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de empleados.
 * @author Tegafadax
 * @version 1.0
 * @since 2025-06-09
 */
@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    /**
     * Crear un nuevo empleado
     */
    @PostMapping
    public ResponseEntity<GETEmpleadoDTO> crearEmpleado(@Valid @RequestBody CrearEmpleadoDTO dto) {
        GETEmpleadoDTO empleado = empleadoService.crearEmpleado(dto);
        return ResponseEntity.ok(empleado);
    }

    /**
     * Actualizar un empleado por ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<GETEmpleadoDTO> actualizarEmpleado(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarEmpleadoCompletoDTO dto) {
        GETEmpleadoDTO actualizado = empleadoService.actualizarEmpleado(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Eliminar (inhabilitar) un empleado por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener un empleado por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GETEmpleadoDTO> obtenerEmpleadoPorId(@PathVariable Integer id) {
        GETEmpleadoDTO empleado = empleadoService.obtenerPorId(id);
        return ResponseEntity.ok(empleado);
    }

    /**
     * Listar todos los empleados
     */
    @GetMapping
    public ResponseEntity<List<GETEmpleadoDTO>> listarTodos() {
        List<GETEmpleadoDTO> empleados = empleadoService.listarTodos();
        return ResponseEntity.ok(empleados);
    }
}
