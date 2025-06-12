package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DAO.InsumoDAO;
import com.bfc.BarFitCixSistema.model.DAO.TipoCantidadDAO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.CrearInsumoDTO;
import com.bfc.BarFitCixSistema.model.DTO.InsumoDTO.GETInsumoDTO;
import com.bfc.BarFitCixSistema.model.entidad.Insumo;
import com.bfc.BarFitCixSistema.model.entidad.TipoCantidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Pruebas unitarias para InsumoServiceImpl usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
class InsumoServiceImplTest {

    @Mock
    private InsumoDAO insumoDAO; // Mock del repositorio de Insumo

    @Mock
    private TipoCantidadDAO tipoCantidadDAO; // Mock del repositorio de TipoCantidad

    @InjectMocks
    private InsumoServiceImpl insumoService; // Inyecta los mocks aquí

    private TipoCantidad tipoCantidad;
    private Insumo insumo;
    private CrearInsumoDTO crearInsumoDTO;

    @BeforeEach
    void setUp() {
        // Preparamos datos de prueba
        tipoCantidad = new TipoCantidad();
        tipoCantidad.setIdTipoCantidad(1);
        tipoCantidad.setNomCantidad("Kg");

        crearInsumoDTO = new CrearInsumoDTO();
        crearInsumoDTO.setNomInsumo("Arroz");
        crearInsumoDTO.setIdTipoCantidad(1);

        insumo = new Insumo();
        insumo.setIdInsumo(1);
        insumo.setNomInsumo("Arroz");
        insumo.setTipoCantidad(tipoCantidad);
    }

    @DisplayName("Test para crear un nuevo insumo")
    @Test
    void crearInsumo_deberiaRetornarInsumoCreado() {
        // Given (Dado)
        // Configuramos el comportamiento de los mocks.
        given(tipoCantidadDAO.findById(1)).willReturn(Optional.of(tipoCantidad));
        given(insumoDAO.save(any(Insumo.class))).willReturn(insumo);

        // When (Cuando)
        // Ejecutamos el método a probar.
        GETInsumoDTO insumoGuardado = insumoService.crearInsumo(crearInsumoDTO);

        // Then (Entonces)
        // Verificamos el resultado.
        assertThat(insumoGuardado).isNotNull();
        assertThat(insumoGuardado.getNomInsumo()).isEqualTo("Arroz");

        // También verificamos que el método save del DAO fue llamado una vez.
        verify(insumoDAO).save(any(Insumo.class));
    }
}