package com.bfc.BarFitCixSistema.model.service.Impl;
import com.bfc.BarFitCixSistema.model.DAO.BoletaDAO;
import com.bfc.BarFitCixSistema.model.DAO.EmpleadoDAO;
import com.bfc.BarFitCixSistema.model.DAO.PedidoDAO;
import com.bfc.BarFitCixSistema.model.DTO.BoletaDTO.BoletaResponseDTO;
import com.bfc.BarFitCixSistema.model.DTO.BoletaDTO.CrearBoletaRequestDTO;
import com.bfc.BarFitCixSistema.model.entidad.*;
import com.bfc.BarFitCixSistema.model.service.BoletaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoletaServiceImpl implements BoletaService {

    private final BoletaDAO boletaDAO;
    private final PedidoDAO pedidoDAO;
    private final EmpleadoDAO empleadoDAO;

    @Override
    @Transactional
    public List<BoletaResponseDTO> generarBoletas(CrearBoletaRequestDTO requestDTO) {
        Pedido pedido = pedidoDAO.findByIdWithDetails(requestDTO.getIdPedido())
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + requestDTO.getIdPedido()));

        if (pedido.getEstado() != Pedido.PedidoStatus.completado) {
            throw new IllegalStateException("Solo se pueden generar boletas para pedidos completados.");
        }

        Empleado empleado = empleadoDAO.findById(requestDTO.getIdEmpleado())
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + requestDTO.getIdEmpleado()));

        BigDecimal totalPedido = calcularTotalPedido(pedido);
        BigDecimal totalPagado = requestDTO.getPagos().stream()
                .map(CrearBoletaRequestDTO.PagoDetalleDTO::getMontoPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPedido.compareTo(totalPagado) != 0) {
            throw new IllegalStateException(String.format("El monto pagado (S/ %.2f) no coincide con el total del pedido (S/ %.2f).", totalPagado, totalPedido));
        }

        List<Boleta> boletasCreadas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (CrearBoletaRequestDTO.PagoDetalleDTO pago : requestDTO.getPagos()) {
            Boleta nuevaBoleta = new Boleta();
            nuevaBoleta.setPedido(pedido);
            nuevaBoleta.setEmpleado(empleado);
            nuevaBoleta.setFechaBoleta(ahora);
            nuevaBoleta.setFechaPago(ahora);
            nuevaBoleta.setNombreCliente(pago.getNomCliente());
            nuevaBoleta.setDniCliente(pago.getDniCliente());
            nuevaBoleta.setMontoPago(pago.getMontoPago());
            nuevaBoleta.setMetodoPago(pago.getMetodoPago());

            boletasCreadas.add(boletaDAO.save(nuevaBoleta));
        }

        List<BoletaResponseDTO> response = new ArrayList<>();
        for (Boleta boleta : boletasCreadas) {
            response.add(convertirAResponseDTO(boleta));
        }
        return response;
    }

    private BigDecimal calcularTotalPedido(Pedido pedido) {
        if (pedido.getDetalles() == null) {
            return BigDecimal.ZERO;
        }
        return pedido.getDetalles().stream()
                .map(detalle -> {
                    ProductoPrecio precio = detalle.getProducto().getPrecioActual();
                    if (precio != null) {
                        return precio.getPrecio().multiply(new BigDecimal(detalle.getCantidad()));
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BoletaResponseDTO convertirAResponseDTO(Boleta boleta) {
        BoletaResponseDTO dto = new BoletaResponseDTO();
        dto.setIdBoleta(boleta.getIdBoleta());
        dto.setFechaBoleta(boleta.getFechaBoleta());
        dto.setFechaPago(boleta.getFechaPago());
        dto.setNomCliente(boleta.getNombreCliente());
        dto.setDniCliente(boleta.getDniCliente());
        dto.setMontoPago(boleta.getMontoPago());
        dto.setMetodoPago(boleta.getMetodoPago());
        dto.setIdPedido(boleta.getPedido().getIdPedido());
        dto.setNombreEmpleado(boleta.getEmpleado().getNom_empleado());
        return dto;
    }
}
