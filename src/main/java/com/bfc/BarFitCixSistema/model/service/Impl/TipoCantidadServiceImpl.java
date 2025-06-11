package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DAO.TipoCantidadDAO;
import com.bfc.BarFitCixSistema.model.DTO.TipoCantidadDTO.GETTipoCantidadDTO;
import com.bfc.BarFitCixSistema.model.entidad.TipoCantidad;
import com.bfc.BarFitCixSistema.model.service.TipoCantidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoCantidadServiceImpl implements TipoCantidadService {

    @Autowired
    private TipoCantidadDAO tipoCantidadDAO;

    @Override
    public List<GETTipoCantidadDTO> listarTodosTipoCantidad() {
        List<TipoCantidad> tipoCantidades = tipoCantidadDAO.findAll();

        return tipoCantidades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private GETTipoCantidadDTO convertirADTO(TipoCantidad tipoCantidad) {
        GETTipoCantidadDTO dto = new GETTipoCantidadDTO();
        dto.setIdTipoCantidad(tipoCantidad.getIdTipoCantidad());
        dto.setNomCantidad(tipoCantidad.getNomCantidad());
        return dto;
    }
}

