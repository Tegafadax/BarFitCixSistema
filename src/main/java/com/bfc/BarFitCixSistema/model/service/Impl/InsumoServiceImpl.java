package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DAO.InsumoDAO;
import com.bfc.BarFitCixSistema.model.DAO.TipoCantidadDAO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.GETInsumoDTO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.CrearInsumoDTO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.ActualizarInsumoDTO;
import com.bfc.BarFitCixSistema.model.entidad.Insumo;
import com.bfc.BarFitCixSistema.model.entidad.TipoCantidad;
import com.bfc.BarFitCixSistema.model.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsumoServiceImpl implements InsumoService {

    @Autowired
    private InsumoDAO insumoDAO;

    @Autowired
    private TipoCantidadDAO tipoCantidadDAO;

    @Override
    public List<GETInsumoDTO> listarTodosInsumos() {
        List<Insumo> insumos = insumoDAO.findAllWithTipoCantidad();
        return insumos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GETInsumoDTO obtenerInsumoPorId(Integer id) {
        Optional<Insumo> insumo = insumoDAO.findById(id);
        if (insumo.isPresent()) {
            return convertirAGETDTO(insumo.get());
        }
        return null;
    }

    @Override
    public GETInsumoDTO crearInsumo(CrearInsumoDTO insumoDTO) {
        // Verificar que el tipo de cantidad existe
        Optional<TipoCantidad> tipoCantidad = tipoCantidadDAO.findById(insumoDTO.getIdTipoCantidad());
        if (!tipoCantidad.isPresent()) {
            throw new RuntimeException("Tipo de cantidad no encontrado");
        }

        Insumo insumo = new Insumo();
        insumo.setNomInsumo(insumoDTO.getNomInsumo());
        insumo.setTipoCantidad(tipoCantidad.get());

        Insumo insumoGuardado = insumoDAO.save(insumo);
        return convertirAGETDTO(insumoGuardado);
    }

    @Override
    public GETInsumoDTO actualizarInsumo(ActualizarInsumoDTO insumoDTO) {
        Optional<Insumo> insumoExistente = insumoDAO.findById(insumoDTO.getIdInsumo());
        if (!insumoExistente.isPresent()) {
            throw new RuntimeException("Insumo no encontrado");
        }

        Optional<TipoCantidad> tipoCantidad = tipoCantidadDAO.findById(insumoDTO.getIdTipoCantidad());
        if (!tipoCantidad.isPresent()) {
            throw new RuntimeException("Tipo de cantidad no encontrado");
        }

        Insumo insumo = insumoExistente.get();
        insumo.setNomInsumo(insumoDTO.getNomInsumo());
        insumo.setTipoCantidad(tipoCantidad.get());

        Insumo insumoActualizado = insumoDAO.save(insumo);
        return convertirAGETDTO(insumoActualizado);
    }

    @Override
    public boolean eliminarInsumo(Integer id) {
        if (insumoDAO.existsById(id)) {
            insumoDAO.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<GETInsumoDTO> buscarInsumosPorTipoCantidad(Integer idTipoCantidad) {
        List<Insumo> insumos = insumoDAO.findByTipoCantidadIdTipoCantidad(idTipoCantidad);
        return insumos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GETInsumoDTO> buscarInsumosPorNombre(String nombre) {
        List<Insumo> insumos = insumoDAO.findByNomInsumoContainingIgnoreCase(nombre);
        return insumos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    private GETInsumoDTO convertirAGETDTO(Insumo insumo) {
        GETInsumoDTO dto = new GETInsumoDTO();
        dto.setIdInsumo(insumo.getIdInsumo());
        dto.setNomInsumo(insumo.getNomInsumo());
        dto.setIdTipoCantidad(insumo.getTipoCantidad().getIdTipoCantidad());
        dto.setNomTipoCantidad(insumo.getTipoCantidad().getNomCantidad());
        return dto;
    }
}