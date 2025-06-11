package com.bfc.BarFitCixSistema.model.service;

import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.ActualizarProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.CambiarPrecioDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.CrearProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.GETProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.HistorialPrecioDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    // CRUD básico de productos
    List<GETProductoDTO> listarTodosProductos();

    GETProductoDTO obtenerProductoPorId(Integer id);

    GETProductoDTO crearProducto(CrearProductoDTO productoDTO);

    GETProductoDTO actualizarProducto(ActualizarProductoDTO productoDTO);

    boolean eliminarProducto(Integer id);

    // Búsquedas
    List<GETProductoDTO> buscarProductosPorNombre(String nombre);

    List<GETProductoDTO> buscarProductosPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax);

    // GESTIÓN DE PRECIOS - NUEVO
    GETProductoDTO cambiarPrecio(CambiarPrecioDTO cambiarPrecioDTO);

    List<HistorialPrecioDTO> obtenerHistorialPrecios(Integer idProducto);

    BigDecimal obtenerPrecioActual(Integer idProducto);
}