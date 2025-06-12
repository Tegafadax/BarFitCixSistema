package com.bfc.BarFitCixSistema.model.service;

import java.io.ByteArrayInputStream;

/**
 * Interfaz para el servicio de generación de reportes.
 */
public interface ReporteService {

    /**
     * Genera un reporte de la lista de empleados en formato Excel.
     * @return Un ByteArrayInputStream que contiene los datos del archivo Excel.
     */
    ByteArrayInputStream generarReporteEmpleadosExcel();
}