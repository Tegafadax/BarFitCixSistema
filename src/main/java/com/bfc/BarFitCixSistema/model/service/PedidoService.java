package com.bfc.BarFitCixSistema.model.service;
import com.bfc.BarFitCixSistema.model.DTO.PedidoDTO.*;

import java.util.List;

/**
 * Interfaz para el servicio de gestión de Pedidos.
 * Define las operaciones de negocio relacionadas con los pedidos.
 */
public interface PedidoService {

    /**
     * Crea un nuevo pedido a partir de los datos proporcionados.
     * @param dto El DTO con la información para crear el pedido y sus detalles.
     * @return El DTO con la información completa del pedido recién creado.
     */
    PedidoResponseDTO crearPedido(CrearPedidoDTO dto);

    /**
     * Obtiene una lista de todos los pedidos para la vista de historial.
     * La lista está ordenada por estado (pendientes primero) y luego por fecha.
     * @return Una lista de DTOs optimizados para el historial.
     */
    List<PedidoHistorialDTO> listarPedidosHistorial();

    /**
     * Obtiene los detalles completos de un único pedido por su ID.
     * @param id El ID del pedido.
     * @return Un DTO con la información detallada del pedido.
     */
    PedidoResponseDTO obtenerPedidoPorId(Integer id);

    /**
     * Finaliza un pedido, cambiando su estado de 'pendiente' a 'completado'.
     * @param id El ID del pedido a finalizar.
     * @return El DTO del pedido con su estado actualizado.
     */
    PedidoResponseDTO finalizarPedido(Integer id);

    /**
     * Actualiza la cantidad de un producto específico dentro de un pedido.
     * @param dto El DTO que contiene los IDs y la nueva cantidad.
     * @return El DTO con el estado actualizado del pedido.
     */
    PedidoResponseDTO actualizarCantidadProducto(ActualizarCantidadRequestDTO dto);

    /**
     * Actualiza el comentario de un producto específico dentro de un pedido.
     * @param dto El DTO que contiene los IDs y el nuevo comentario.
     * @return El DTO con el estado actualizado del pedido.
     */
    PedidoResponseDTO actualizarComentarioProducto(ActualizarComentarioRequestDTO dto);

    /**
     * Elimina un pedido por su ID.
     * @param id El ID del pedido a eliminar.
     */
    void eliminarPedido(Integer id);
}