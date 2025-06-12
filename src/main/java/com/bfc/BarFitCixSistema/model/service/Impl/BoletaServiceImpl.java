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
    // Se elimina la inyección de PedidoServiceImpl para evitar dependencia circular

    @Override
    @Transactional
    public List<BoletaResponseDTO> generarBoletas(CrearBoletaRequestDTO requestDTO) {
        // 1. Validar que el pedido exista y obtener sus detalles.
        Pedido pedido = pedidoDAO.findByIdWithDetails(requestDTO.getIdPedido())
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + requestDTO.getIdPedido()));

        // 2. Validar que el pedido esté completado.
        if (pedido.getEstado() != Pedido.PedidoStatus.completado) {
            throw new IllegalStateException("Solo se pueden generar boletas para pedidos completados.");
        }

        // 3. Validar que el empleado exista.
        Empleado empleado = empleadoDAO.findById(requestDTO.getIdEmpleado())
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + requestDTO.getIdEmpleado()));

        // 4. Calcular el total real del pedido y el total de los pagos recibidos.
        // CORRECCIÓN: Se calcula el total directamente aquí para evitar la dependencia circular.
        BigDecimal totalPedido = calcularTotalPedido(pedido);
        BigDecimal totalPagado = requestDTO.getPagos().stream()
                .map(CrearBoletaRequestDTO.PagoDetalleDTO::getMontoPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. Validar que el monto pagado coincida con el total del pedido.
        if (totalPedido.compareTo(totalPagado) != 0) {
            throw new IllegalStateException(String.format("El monto pagado (S/ %.2f) no coincide con el total del pedido (S/ %.2f).", totalPagado, totalPedido));
        }

        // 6. Crear y guardar una boleta por cada detalle de pago.
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
            nuevaBoleta.setMontoPago(pago.getMontoPago().floatValue());
            nuevaBoleta.setMetodoPago(pago.getMetodoPago());

            boletasCreadas.add(boletaDAO.save(nuevaBoleta));
        }

        // 7. Convertir las entidades guardadas a DTOs de respuesta.
        List<BoletaResponseDTO> response = new ArrayList<>();
        for (Boleta boleta : boletasCreadas) {
            response.add(convertirAResponseDTO(boleta));
        }
        return response;
    }

    /**
     * Calcula el total de un pedido sumando los subtotales de sus detalles.
     * Este método es una réplica de la lógica en PedidoServiceImpl para evitar
     * la inyección de dependencias entre servicios.
     */
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
        dto.setNomCliente(boleta.getNombreCliente());
        dto.setDniCliente(boleta.getDniCliente());
        dto.setMontoPago(new BigDecimal(Float.toString(boleta.getMontoPago())));
        dto.setMetodoPago(boleta.getMetodoPago());
        dto.setIdPedido(boleta.getPedido().getIdPedido());
        dto.setNombreEmpleado(boleta.getEmpleado().getNom_empleado());
        return dto;
    }
}