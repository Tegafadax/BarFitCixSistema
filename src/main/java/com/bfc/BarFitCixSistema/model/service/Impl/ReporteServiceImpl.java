package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DTO.EmpleadoDTO.GETEmpleadoDTO;
import com.bfc.BarFitCixSistema.model.service.EmpleadoService;
import com.bfc.BarFitCixSistema.model.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Implementación del servicio de reportes.
 * Utiliza Apache POI para crear archivos de Excel.
 */
@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    // Inyectamos el EmpleadoService que ya existe para obtener los datos
    private final EmpleadoService empleadoService;

    @Override
    public ByteArrayInputStream generarReporteEmpleadosExcel() {
        // Definimos las columnas para nuestro archivo Excel
        String[] COLUMNS = {"ID", "Usuario", "Correo Electrónico", "Rol", "Estado"};

        // Usamos try-with-resources para asegurarnos de que los recursos se cierren solos
        try (
                Workbook workbook = new XSSFWorkbook(); // Creamos un nuevo libro de Excel
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet("Lista de Empleados");

            // Estilo para la cabecera (negrita)
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Fila de Cabecera
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < COLUMNS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNS[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // Obtenemos la lista de empleados usando el servicio existente
            List<GETEmpleadoDTO> empleados = empleadoService.listarTodos();

            int rowIdx = 1;
            for (GETEmpleadoDTO empleado : empleados) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(empleado.getId());
                row.createCell(1).setCellValue(empleado.getNombreUsuario());
                row.createCell(2).setCellValue(empleado.getCorreoElectronico());
                row.createCell(3).setCellValue(empleado.getRol());
                row.createCell(4).setCellValue(empleado.getEstado());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Falló al generar el reporte de empleados: " + e.getMessage());
        }
    }
}