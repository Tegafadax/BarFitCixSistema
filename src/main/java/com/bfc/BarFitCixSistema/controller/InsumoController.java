package com.bfc.BarFitCixSistema.controller;

import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.ActualizarInsumoDTO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.CrearInsumoDTO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.GETInsumoDTO;

import com.bfc.BarFitCixSistema.model.service.InsumoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumo")
@CrossOrigin(origins = "*")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    @GetMapping("/listar")
    public ResponseEntity<List<GETInsumoDTO>> listarTodosInsumos() {
        try {
            List<GETInsumoDTO> insumos = insumoService.listarTodosInsumos();
            return ResponseEntity.ok(insumos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GETInsumoDTO> obtenerInsumoPorId(@PathVariable Integer id) {
        try {
            GETInsumoDTO insumo = insumoService.obtenerInsumoPorId(id);
            if (insumo != null) {
                return ResponseEntity.ok(insumo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<GETInsumoDTO> crearInsumo(@RequestBody CrearInsumoDTO insumoDTO) {
        try {
            GETInsumoDTO nuevoInsumo = insumoService.crearInsumo(insumoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInsumo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<GETInsumoDTO> actualizarInsumo(@RequestBody ActualizarInsumoDTO insumoDTO) {
        try {
            GETInsumoDTO insumoActualizado = insumoService.actualizarInsumo(insumoDTO);
            return ResponseEntity.ok(insumoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInsumo(@PathVariable Integer id) {
        try {
            boolean eliminado = insumoService.eliminarInsumo(id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar-por-tipo/{idTipoCantidad}")
    public ResponseEntity<List<GETInsumoDTO>> buscarInsumosPorTipoCantidad(@PathVariable Integer idTipoCantidad) {
        try {
            List<GETInsumoDTO> insumos = insumoService.buscarInsumosPorTipoCantidad(idTipoCantidad);
            return ResponseEntity.ok(insumos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<GETInsumoDTO>> buscarInsumosPorNombre(@RequestParam String nombre) {
        try {
            List<GETInsumoDTO> insumos = insumoService.buscarInsumosPorNombre(nombre);
            return ResponseEntity.ok(insumos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}