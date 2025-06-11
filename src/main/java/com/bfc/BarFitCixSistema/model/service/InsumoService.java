package com.bfc.BarFitCixSistema.model.service;

import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.GETInsumoDTO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.CrearInsumoDTO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.ActualizarInsumoDTO;

import java.util.List;

public interface InsumoService {

    List<GETInsumoDTO> listarTodosInsumos();

    GETInsumoDTO obtenerInsumoPorId(Integer id);

    GETInsumoDTO crearInsumo(CrearInsumoDTO insumoDTO);

    GETInsumoDTO actualizarInsumo(ActualizarInsumoDTO insumoDTO);

    boolean eliminarInsumo(Integer id);

    List<GETInsumoDTO> buscarInsumosPorTipoCantidad(Integer idTipoCantidad);

    List<GETInsumoDTO> buscarInsumosPorNombre(String nombre);
}