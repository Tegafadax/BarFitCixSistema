package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DAO.EmpleadoDAO;
import com.bfc.BarFitCixSistema.model.DAO.InsumoDAO;
import com.bfc.BarFitCixSistema.model.DAO.ProductoDAO;
import com.bfc.BarFitCixSistema.model.DAO.ProductoPrecioDAO;
import com.bfc.BarFitCixSistema.model.DAO.TipoCantidadDAO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.*;
import com.bfc.BarFitCixSistema.model.entidad.*;
import com.bfc.BarFitCixSistema.model.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private ProductoPrecioDAO productoPrecioDAO;

    @Autowired
    private InsumoDAO insumoDAO;

    @Autowired
    private TipoCantidadDAO tipoCantidadDAO;

    @Autowired
    private EmpleadoDAO empleadoDAO;

    @Override
    public List<GETProductoDTO> listarTodosProductos() {
        List<Producto> productos = productoDAO.findAllWithPreciosActivosAndInsumos();
        return productos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GETProductoDTO obtenerProductoPorId(Integer id) {
        Producto producto = productoDAO.findByIdWithPrecioActivoAndInsumos(id);
        return producto != null ? convertirAGETDTO(producto) : null;
    }

    @Override
    public GETProductoDTO crearProducto(CrearProductoDTO productoDTO) {
        // Verificar que el empleado existe
        Optional<Empleado> empleado = empleadoDAO.findById(productoDTO.getIdEmpleado());
        if (!empleado.isPresent()) {
            throw new RuntimeException("Empleado no encontrado: " + productoDTO.getIdEmpleado());
        }

        // Crear la entidad Producto (SIN PRECIO)
        Producto producto = Producto.builder()
                .nomProducto(productoDTO.getNomProducto())
                .build();

        // Crear la lista de ProductoInsumo (MANTENER IGUAL QUE ANTES)
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
                    .id(new ProductoInsumoPK(null, insumoDTO.getIdInsumo()))
                    .cantPorInsumo(insumoDTO.getCantPorInsumo())
                    .producto(producto)
                    .insumo(insumo.get())
                    .tipoCantidad(tipoCantidad.get())
                    .build();

            productosInsumos.add(productoInsumo);
        }

        // Asignar los insumos al producto
        producto.setInsumos(productosInsumos);

        // Guardar producto primero
        Producto productoGuardado = productoDAO.save(producto);

        // Actualizar los IDs en ProductoInsumo después del guardado
        for (ProductoInsumo pi : productoGuardado.getInsumos()) {
            pi.getId().setIdProducto(productoGuardado.getIdProducto());
        }

        // CREAR PRECIO INICIAL
        ProductoPrecio precioInicial = ProductoPrecio.builder()
                .precio(productoDTO.getPrecioInicial())
                .producto(productoGuardado)
                .empleadoModifico(empleado.get())
                .activo(true)
                .build();

        productoPrecioDAO.save(precioInicial);

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

        // TODO: Implementar actualización de insumos si es necesario
        // Por ahora solo actualizamos el nombre del producto

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
    public List<GETProductoDTO> buscarProductosPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        List<Producto> productos = productoDAO.findByRangoPrecio(precioMin, precioMax);
        return productos.stream()
                .map(this::convertirAGETDTO)
                .collect(Collectors.toList());
    }

    // ===================== GESTIÓN DE PRECIOS =====================

    @Override
    public GETProductoDTO cambiarPrecio(CambiarPrecioDTO cambiarPrecioDTO) {
        // Verificar que el producto existe
        Optional<Producto> producto = productoDAO.findById(cambiarPrecioDTO.getIdProducto());
        if (!producto.isPresent()) {
            throw new RuntimeException("Producto no encontrado");
        }

        // Verificar que el empleado existe
        Optional<Empleado> empleado = empleadoDAO.findById(cambiarPrecioDTO.getIdEmpleado());
        if (!empleado.isPresent()) {
            throw new RuntimeException("Empleado no encontrado: " + cambiarPrecioDTO.getIdEmpleado());
        }

        // Cerrar precio anterior
        productoPrecioDAO.cerrarPrecioAnterior(
                cambiarPrecioDTO.getIdProducto(),
                LocalDateTime.now()
        );

        // Crear nuevo precio
        ProductoPrecio nuevoPrecio = ProductoPrecio.builder()
                .precio(cambiarPrecioDTO.getNuevoPrecio())
                .producto(producto.get())
                .empleadoModifico(empleado.get())
                .activo(true)
                .build();

        productoPrecioDAO.save(nuevoPrecio);

        return convertirAGETDTO(producto.get());
    }

    @Override
    public List<HistorialPrecioDTO> obtenerHistorialPrecios(Integer idProducto) {
        List<ProductoPrecio> historial = productoPrecioDAO.findHistorialByProducto(idProducto);

        return historial.stream()
                .map(pp -> {
                    HistorialPrecioDTO dto = new HistorialPrecioDTO();
                    dto.setIdPrecio(pp.getIdPrecio());
                    dto.setPrecio(pp.getPrecio());
                    dto.setFechaInicio(pp.getFechaInicio());
                    dto.setFechaFin(pp.getFechaFin());
                    dto.setActivo(pp.getActivo());
                    dto.setEmpleadoModifico(pp.getEmpleadoModifico().getNom_empleado());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal obtenerPrecioActual(Integer idProducto) {
        Optional<ProductoPrecio> precioActivo = productoPrecioDAO.findPrecioActivoByProducto(idProducto);
        return precioActivo.map(ProductoPrecio::getPrecio).orElse(BigDecimal.ZERO);
    }

    // ===================== MÉTODO HELPER =====================

    private GETProductoDTO convertirAGETDTO(Producto producto) {
        GETProductoDTO dto = new GETProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNomProducto(producto.getNomProducto());

        // Obtener precio actual
        ProductoPrecio precioActual = producto.getPrecioActual();
        if (precioActual != null) {
            dto.setPrecioActual(precioActual.getPrecio());
            dto.setFechaPrecioActual(precioActual.getFechaInicio());
            dto.setEmpleadoModificoPrecio(precioActual.getEmpleadoModifico().getNom_empleado());
        }

        // Convertir la lista de insumos (MANTENER IGUAL QUE ANTES)
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