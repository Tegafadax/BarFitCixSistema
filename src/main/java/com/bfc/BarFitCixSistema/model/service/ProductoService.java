package com.bfc.BarFitCixSistema.model.service;

import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.ActualizarProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.CrearProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.GETProductoDTO;

import java.util.List;

public interface ProductoService {

    List<GETProductoDTO> listarTodosProductos();

    GETProductoDTO obtenerProductoPorId(Integer id);

    GETProductoDTO crearProducto(CrearProductoDTO productoDTO);

    GETProductoDTO actualizarProducto(ActualizarProductoDTO productoDTO);

    boolean eliminarProducto(Integer id);

    List<GETProductoDTO> buscarProductosPorNombre(String nombre);

    List<GETProductoDTO> listarProductosActivos();

    List<GETProductoDTO> listarProductosInactivos();
}