package com.bfc.BarFitCixSistema.controller;
import com.bfc.BarFitCixSistema.model.DTO.PedidoDTO.*;
import com.bfc.BarFitCixSistema.model.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de Pedidos.
 * Expone los endpoints para crear, leer, actualizar y eliminar pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Endpoint para crear un nuevo pedido.
     */
    @PostMapping
    public ResponseEntity<?> crearPedido(@Valid @RequestBody CrearPedidoDTO dto) {
        try {
            PedidoResponseDTO nuevoPedido = pedidoService.crearPedido(dto);
            return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al crear el pedido: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener la lista de todos los pedidos para el historial.
     */
    @GetMapping
    public ResponseEntity<List<PedidoHistorialDTO>> listarPedidosHistorial() {
        List<PedidoHistorialDTO> historial = pedidoService.listarPedidosHistorial();
        return ResponseEntity.ok(historial);
    }

    /**
     * Endpoint para obtener los detalles completos de un pedido por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPedidoPorId(@PathVariable Integer id) {
        try {
            PedidoResponseDTO pedido = pedidoService.obtenerPedidoPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para finalizar un pedido.
     */
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarPedido(@PathVariable Integer id) {
        try {
            PedidoResponseDTO pedidoFinalizado = pedidoService.finalizarPedido(id);
            return ResponseEntity.ok(pedidoFinalizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para actualizar la cantidad de un producto en un pedido.
     */
    @PutMapping("/actualizar-cantidad")
    public ResponseEntity<?> actualizarCantidad(@Valid @RequestBody ActualizarCantidadRequestDTO dto) {
        try {
            PedidoResponseDTO pedidoActualizado = pedidoService.actualizarCantidadProducto(dto);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para actualizar el comentario de un producto en un pedido.
     */
    @PutMapping("/actualizar-comentario")
    public ResponseEntity<?> actualizarComentario(@Valid @RequestBody ActualizarComentarioRequestDTO dto) {
        try {
            PedidoResponseDTO pedidoActualizado = pedidoService.actualizarComentarioProducto(dto);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para eliminar un pedido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Integer id) {
        try {
            pedidoService.eliminarPedido(id);
            return ResponseEntity.ok(Map.of("mensaje", "Pedido con ID " + id + " eliminado correctamente."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}