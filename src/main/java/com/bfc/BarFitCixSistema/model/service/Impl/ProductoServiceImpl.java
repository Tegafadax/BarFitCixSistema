package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DAO.InsumoDAO;
import com.bfc.BarFitCixSistema.model.DAO.ProductoDAO;
import com.bfc.BarFitCixSistema.model.DAO.TipoCantidadDAO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.ActualizarProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.CrearProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.GETProductoDTO;
import com.bfc.BarFitCixSistema.model.entidad.*;
import com.bfc.BarFitCixSistema.model.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoDAO productoDAO;

    @Autowired
    private InsumoDAO insumoDAO;

    @Autowired
    private TipoCantidadDAO tipoCantidadDAO;

    @Override
    public List<GETProductoDTO> listarTodosProductos() {
        List<Producto> productos = productoDAO.findAllWithInsumos();
        return productos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GETProductoDTO obtenerProductoPorId(Integer id) {
        Producto producto = productoDAO.findByIdWithInsumos(id);
        return producto != null ? convertirAGETDTO(producto) : null;
    }

    @Override
    public GETProductoDTO crearProducto(CrearProductoDTO productoDTO) {
        // Crear la entidad Producto
        Producto producto = Producto.builder()
                .nomProducto(productoDTO.getNomProducto())
                .precioDeProductos(productoDTO.getPrecioDeProductos())
                .build();

        // Crear la lista de ProductoInsumo
        List<ProductoInsumo> productosInsumos = new ArrayList<>();

        for (CrearProductoDTO.InsumoDetalleDTO insumoDTO : productoDTO.getInsumos()) {
            // Verificar que el insumo existe
            Optional<Insumo> insumo = insumoDAO.findById(insumoDTO.getIdInsumo());
            if (!insumo.isPresent()) {
                throw new RuntimeException("Insumo no encontrado: " + insumoDTO.getIdInsumo());
            }

            // Verificar que el tipo de cantidad existe
            Optional<TipoCantidad> tipoCantidad = tipoCantidadDAO.findById(insumoDTO.getIdTipoCantidad());
            if (!tipoCantidad.isPresent()) {
                throw new RuntimeException("Tipo de cantidad no encontrado: " + insumoDTO.getIdTipoCantidad());
            }

            // Crear ProductoInsumo
            ProductoInsumo productoInsumo = ProductoInsumo.builder()
                    .id(new ProductoInsumoPK(null, insumoDTO.getIdInsumo())) // El idProducto se asignará después
                    .cantPorInsumo(insumoDTO.getCantPorInsumo())
                    .producto(producto)
                    .insumo(insumo.get())
                    .tipoCantidad(tipoCantidad.get())
                    .build();

            productosInsumos.add(productoInsumo);
        }

        // Asignar los insumos al producto
        producto.setInsumos(productosInsumos);

        // Guardar (CascadeType.PERSIST guardará también los ProductoInsumo)
        Producto productoGuardado = productoDAO.save(producto);

        // Actualizar los IDs en ProductoInsumo después del guardado
        for (ProductoInsumo pi : productoGuardado.getInsumos()) {
            pi.getId().setIdProducto(productoGuardado.getIdProducto());
        }

        return convertirAGETDTO(productoGuardado);
    }

    @Override
    public GETProductoDTO actualizarProducto(ActualizarProductoDTO productoDTO) {
        Optional<Producto> productoExistente = productoDAO.findById(productoDTO.getIdProducto());
        if (!productoExistente.isPresent()) {
            throw new RuntimeException("Producto no encontrado");
        }

        Producto producto = productoExistente.get();
        producto.setNomProducto(productoDTO.getNomProducto());
        producto.setPrecioDeProductos(productoDTO.getPrecioDeProductos());
        producto.setFecFin(productoDTO.getFecFin());

        // TODO: Implementar actualización de insumos (requiere lógica más compleja)
        // Por ahora solo actualizamos los datos básicos del producto

        Producto productoActualizado = productoDAO.save(producto);
        return convertirAGETDTO(productoActualizado);
    }

    @Override
    public boolean eliminarProducto(Integer id) {
        if (productoDAO.existsById(id)) {
            productoDAO.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<GETProductoDTO> buscarProductosPorNombre(String nombre) {
        List<Producto> productos = productoDAO.findByNomProductoContainingIgnoreCase(nombre);
        return productos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GETProductoDTO> listarProductosActivos() {
        List<Producto> productos = productoDAO.findByFecFinIsNull();
        return productos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GETProductoDTO> listarProductosInactivos() {
        List<Producto> productos = productoDAO.findByFecFinIsNotNull();
        return productos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    private GETProductoDTO convertirAGETDTO(Producto producto) {
        GETProductoDTO dto = new GETProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNomProducto(producto.getNomProducto());
        dto.setPrecioDeProductos(producto.getPrecioDeProductos());
        dto.setFecInicio(producto.getFecInicio());
        dto.setFecFin(producto.getFecFin());

        // Convertir la lista de insumos
        if (producto.getInsumos() != null) {
            List<GETProductoDTO.ProductoInsumoDTO> insumosDTO = producto.getInsumos().stream()
                    .map(pi -> {
                        GETProductoDTO.ProductoInsumoDTO insumoDTO = new GETProductoDTO.ProductoInsumoDTO();
                        insumoDTO.setIdInsumo(pi.getInsumo().getIdInsumo());
                        insumoDTO.setNomInsumo(pi.getInsumo().getNomInsumo());
                        insumoDTO.setCantPorInsumo(pi.getCantPorInsumo());
                        insumoDTO.setNomTipoCantidad(pi.getTipoCantidad().getNomCantidad());
                        return insumoDTO;
                    })
                    .collect(Collectors.toList());
            dto.setInsumos(insumosDTO);
        }

        return dto;
    }
}