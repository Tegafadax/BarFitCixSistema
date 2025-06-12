package com.bfc.BarFitCixSistema.model.service;

import com.bfc.BarFitCixSistema.model.DTO.BoletaDTO.BoletaResponseDTO;
import com.bfc.BarFitCixSistema.model.DTO.BoletaDTO.CrearBoletaRequestDTO;

import java.util.List;

/**
 * Interfaz para el servicio de gestión de Boletas.
 */
public interface BoletaService {

    /**
     * Genera una o más boletas para un pedido, manejando pagos únicos y fraccionados.
     * @param requestDTO DTO con los detalles del pedido y los pagos a realizar.
     * @return Una lista de DTOs con la información de cada boleta creada.
     */
    List<BoletaResponseDTO> generarBoletas(CrearBoletaRequestDTO requestDTO);
}