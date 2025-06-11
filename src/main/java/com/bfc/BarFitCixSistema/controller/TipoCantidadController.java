package com.bfc.BarFitCixSistema.controller;

import com.bfc.BarFitCixSistema.model.DTO.TipoCantidadDTO.GETTipoCantidadDTO;
import com.bfc.BarFitCixSistema.model.service.TipoCantidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-cantidad")
@CrossOrigin(origins = "*")
public class TipoCantidadController {

    @Autowired
    private TipoCantidadService tipoCantidadService;

    @GetMapping("/listar")
    public ResponseEntity<List<GETTipoCantidadDTO>> listarTodosTipoCantidad() {
        try {
            List<GETTipoCantidadDTO> tipoCantidades = tipoCantidadService.listarTodosTipoCantidad();
            return ResponseEntity.ok(tipoCantidades);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

