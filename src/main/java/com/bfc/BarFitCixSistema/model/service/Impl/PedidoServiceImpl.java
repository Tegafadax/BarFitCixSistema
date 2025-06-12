package com.bfc.BarFitCixSistema.model.service.Impl;
import com.bfc.BarFitCixSistema.model.DAO.EmpleadoDAO;
import com.bfc.BarFitCixSistema.model.DAO.PedidoDAO;
import com.bfc.BarFitCixSistema.model.DAO.ProductoDAO;
import com.bfc.BarFitCixSistema.model.DTO.PedidoDTO.*;
import com.bfc.BarFitCixSistema.model.entidad.*;
import com.bfc.BarFitCixSistema.model.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoDAO pedidoDAO;
    private final ProductoDAO productoDAO;
    private final EmpleadoDAO empleadoDAO;

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(CrearPedidoDTO dto) {
        Empleado empleado = empleadoDAO.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + dto.getIdEmpleado()));

        Pedido pedido = new Pedido();
        pedido.setMesa(dto.getMesa());
        pedido.setSala(dto.getSala());
        pedido.setEmpleado(empleado);
        pedido.setEstado(Pedido.PedidoStatus.pendiente);
        pedido.setDetalles(new ArrayList<>());

        for (CrearPedidoDTO.PedidoDetalleDTO detalleDTO : dto.getDetalles()) {
            Producto producto = productoDAO.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + detalleDTO.getIdProducto()));

            Subtotal subtotal = new Subtotal();
            subtotal.setId(new PedidoSubtotalPK(null, producto.getIdProducto()));
            subtotal.setPedido(pedido);
            subtotal.setProducto(producto);
            subtotal.setCantidad(detalleDTO.getCantidad());
            subtotal.setComentario(detalleDTO.getComentario());

            pedido.getDetalles().add(subtotal);
        }

        Pedido pedidoGuardado = pedidoDAO.save(pedido);
        return convertirAPedidoResponseDTO(pedidoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoHistorialDTO> listarPedidosHistorial() {
        List<Pedido> pedidos = pedidoDAO.findAllOrderedByStatusAndDate();
        return pedidos.stream()
                .map(this::convertirAPedidoHistorialDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoPorId(Integer id) {
        Pedido pedido = pedidoDAO.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));
        return convertirAPedidoResponseDTO(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO finalizarPedido(Integer id) {
        Pedido pedido = pedidoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

        if (pedido.getEstado() == Pedido.PedidoStatus.completado) {
            throw new IllegalStateException("El pedido ya se encuentra finalizado.");
        }

        pedido.setEstado(Pedido.PedidoStatus.completado);
        Pedido pedidoFinalizado = pedidoDAO.save(pedido);
        return convertirAPedidoResponseDTO(pedidoFinalizado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO actualizarCantidadProducto(ActualizarCantidadRequestDTO dto) {
        Pedido pedido = pedidoDAO.findByIdWithDetails(dto.getIdPedido())
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + dto.getIdPedido()));

        Subtotal subtotal = pedido.getDetalles().stream()
                .filter(d -> d.getProducto().getIdProducto().equals(dto.getIdProducto()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + dto.getIdProducto() + " no encontrado en el pedido."));

        subtotal.setCantidad(dto.getNuevaCantidad());

        Pedido pedidoActualizado = pedidoDAO.save(pedido);
        return convertirAPedidoResponseDTO(pedidoActualizado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO actualizarComentarioProducto(ActualizarComentarioRequestDTO dto) {
        Pedido pedido = pedidoDAO.findByIdWithDetails(dto.getIdPedido())
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + dto.getIdPedido()));

        Subtotal subtotal = pedido.getDetalles().stream()
                .filter(d -> d.getProducto().getIdProducto().equals(dto.getIdProducto()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + dto.getIdProducto() + " no encontrado en el pedido."));

        subtotal.setComentario(dto.getComentario());

        Pedido pedidoActualizado = pedidoDAO.save(pedido);
        return convertirAPedidoResponseDTO(pedidoActualizado);
    }

    @Override
    @Transactional
    public void eliminarPedido(Integer id) {
        if (!pedidoDAO.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar, pedido no encontrado con ID: " + id);
        }
        pedidoDAO.deleteById(id);
    }

    private PedidoResponseDTO convertirAPedidoResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setMesa(pedido.getMesa());
        dto.setSala(pedido.getSala());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setEstado(pedido.getEstado().name());
        dto.setNombreEmpleado(pedido.getEmpleado().getNom_empleado());

        if (pedido.getDetalles() == null) {
            pedido.setDetalles(new ArrayList<>());
        }

        List<PedidoResponseDTO.SubtotalResponseDTO> detallesDTO = pedido.getDetalles().stream()
                .map(detalle -> {
                    PedidoResponseDTO.SubtotalResponseDTO detalleDTO = new PedidoResponseDTO.SubtotalResponseDTO();
                    Producto producto = detalle.getProducto();
                    ProductoPrecio precio = producto.getPrecioActual();

                    detalleDTO.setIdProducto(producto.getIdProducto());
                    detalleDTO.setNombreProducto(producto.getNomProducto());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setComentario(detalle.getComentario());

                    if (precio != null) {
                        BigDecimal precioUnitario = precio.getPrecio();
                        detalleDTO.setPrecioUnitario(precioUnitario);
                        detalleDTO.setSubtotal(precioUnitario.multiply(new BigDecimal(detalle.getCantidad())));
                    } else {
                        detalleDTO.setPrecioUnitario(BigDecimal.ZERO);
                        detalleDTO.setSubtotal(BigDecimal.ZERO);
                    }
                    return detalleDTO;
                }).collect(Collectors.toList());

        dto.setDetalles(detallesDTO);

        BigDecimal total = detallesDTO.stream()
                .map(PedidoResponseDTO.SubtotalResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalPedido(total);

        return dto;
    }

    private PedidoHistorialDTO convertirAPedidoHistorialDTO(Pedido pedido) {
        PedidoHistorialDTO dto = new PedidoHistorialDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setSala(pedido.getSala());
        dto.setNombreAtendido(pedido.getEmpleado().getNom_empleado());
        dto.setMesa(pedido.getMesa());
        dto.setFecha(pedido.getFechaCreacion());
        dto.setEstado(pedido.getEstado().name().toUpperCase());

        if (pedido.getDetalles() == null) {
            pedido.setDetalles(new ArrayList<>());
        }

        BigDecimal total = pedido.getDetalles().stream()
                .map(d -> {
                    ProductoPrecio precio = d.getProducto().getPrecioActual();
                    if (precio != null) {
                        return precio.getPrecio().multiply(new BigDecimal(d.getCantidad()));
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotal(total);

        return dto;
    }
}