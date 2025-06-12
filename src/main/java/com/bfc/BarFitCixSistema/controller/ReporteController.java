package com.bfc.BarFitCixSistema.controller;

import com.bfc.BarFitCixSistema.model.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

/**
 * Controlador para la generación y descarga de reportes.
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * Endpoint para descargar la lista de empleados en formato Excel.
     * Al acceder a esta URL, se iniciará la descarga del archivo.
     */
    @GetMapping("/empleados/excel")
    public ResponseEntity<InputStreamResource> exportarEmpleadosAExcel() {
        ByteArrayInputStream bis = reporteService.generarReporteEmpleadosExcel();

        HttpHeaders headers = new HttpHeaders();
        // Esta cabecera le dice al navegador que debe descargar el archivo
        headers.add("Content-Disposition", "attachment; filename=lista_empleados.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bis));
    }
}