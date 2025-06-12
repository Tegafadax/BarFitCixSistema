package com.bfc.BarFitCixSistema.controller;

import com.bfc.BarFitCixSistema.model.DTO.BoletaDTO.BoletaResponseDTO;
import com.bfc.BarFitCixSistema.model.DTO.BoletaDTO.CrearBoletaRequestDTO;
import com.bfc.BarFitCixSistema.model.service.BoletaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boletas")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaService boletaService;

    /**
     * Endpoint para generar una o más boletas para un pedido.
     * @param requestDTO DTO con la información del pedido y los detalles de pago.
     * @return Una lista de las boletas generadas.
     */
    @PostMapping
    public ResponseEntity<?> generarBoletas(@Valid @RequestBody CrearBoletaRequestDTO requestDTO) {
        try {
            List<BoletaResponseDTO> boletasGeneradas = boletaService.generarBoletas(requestDTO);
            return new ResponseEntity<>(boletasGeneradas, HttpStatus.CREATED);
        } catch (EntityNotFoundException | IllegalStateException e) {
            // Errores de negocio (pedido no encontrado, monto no coincide, etc.)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Errores inesperados del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Ocurrió un error inesperado al generar la boleta."));
        }
    }
}