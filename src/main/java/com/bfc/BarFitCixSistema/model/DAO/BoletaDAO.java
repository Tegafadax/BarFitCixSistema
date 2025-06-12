package com.bfc.BarFitCixSistema.model.DAO;

import com.bfc.BarFitCixSistema.model.entidad.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletaDAO extends JpaRepository<Boleta, Integer> {

    /**
     * Busca todas las boletas asociadas a un ID de pedido específico.
     * Útil para verificar si ya se generaron boletas para un pedido.
     *
     * @param idPedido El ID del pedido.
     * @return Una lista de boletas asociadas a ese pedido.
     */
    List<Boleta> findByPedido_IdPedido(Integer idPedido);
}