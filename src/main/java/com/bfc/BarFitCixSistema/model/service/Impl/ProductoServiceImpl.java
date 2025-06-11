package com.bfc.BarFitCixSistema.model.service.Impl;

import com.bfc.BarFitCixSistema.model.DAO.EmpleadoDAO;
import com.bfc.BarFitCixSistema.model.DAO.InsumoDAO;
import com.bfc.BarFitCixSistema.model.DAO.ProductoDAO;
import com.bfc.BarFitCixSistema.model.DAO.ProductoPrecioDAO;
import com.bfc.BarFitCixSistema.model.DAO.TipoCantidadDAO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.*;
import com.bfc.BarFitCixSistema.model.entidad.*;
import com.bfc.BarFitCixSistema.model.service.ProductoService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
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


    @Autowired
    private EntityManager entityManager;

    @Transactional
    public GETProductoDTO actualizarInsumosDirecto(Integer idProducto, List<CrearProductoDTO.InsumoDetalleDTO> nuevosInsumos) {
        try {
            // 1. Verificar que el producto existe
            Producto producto = productoDAO.findByIdBasic(idProducto);
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + idProducto);
            }

            // 2. Eliminar insumos existentes con query nativa
            int insumosEliminados = entityManager.createQuery(
                            "DELETE FROM ProductoInsumo pi WHERE pi.producto.idProducto = :idProducto")
                    .setParameter("idProducto", idProducto)
                    .executeUpdate();

            log.info("Eliminados {} insumos existentes del producto {}", insumosEliminados, idProducto);

            // 3. Flush para asegurar que se ejecute la eliminación
            entityManager.flush();

            // 4. Crear y guardar nuevos insumos
            for (CrearProductoDTO.InsumoDetalleDTO insumoDTO : nuevosInsumos) {
                // Validaciones
                if (insumoDTO.getCantPorInsumo() <= 0) {
                    throw new RuntimeException("La cantidad del insumo debe ser mayor a 0");
                }

                Insumo insumo = insumoDAO.findById(insumoDTO.getIdInsumo())
                        .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + insumoDTO.getIdInsumo()));

                TipoCantidad tipoCantidad = tipoCantidadDAO.findById(insumoDTO.getIdTipoCantidad())
                        .orElseThrow(() -> new RuntimeException("Tipo de cantidad no encontrado con ID: " + insumoDTO.getIdTipoCantidad()));

                // Crear nuevo ProductoInsumo
                ProductoInsumo productoInsumo = ProductoInsumo.builder()
                        .id(new ProductoInsumoPK(idProducto, insumo.getIdInsumo()))
                        .cantPorInsumo(insumoDTO.getCantPorInsumo())
                        .producto(producto)
                        .insumo(insumo)
                        .tipoCantidad(tipoCantidad)
                        .build();

                // Guardar directamente con EntityManager
                entityManager.persist(productoInsumo);
            }

            // 5. Flush para asegurar que se guarden los nuevos insumos
            entityManager.flush();

            log.info("Creados {} nuevos insumos para el producto {}", nuevosInsumos.size(), idProducto);

            // 6. Retornar producto actualizado
            return obtenerProductoCompleto(idProducto);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno al actualizar insumos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<GETProductoDTO> listarTodosProductos() {
        // CAMBIO: Ahora solo devuelve productos con precio activo
        List<Producto> productos = productoDAO.findAllBasic();

        return productos.stream()
                .map(producto -> obtenerProductoCompleto(producto.getIdProducto()))
                .filter(dto -> dto != null && dto.getPrecioActual() != null) // Doble filtro de seguridad
                .collect(Collectors.toList());
    }

    // NUEVO MÉTODO: Para listar TODOS los productos (admin)
    public List<GETProductoDTO> listarTodosProductosAdmin() {
        // Para uso administrativo - incluye productos sin precio
        List<Producto> productos = productoDAO.findAllProductos();

        return productos.stream()
                .map(producto -> obtenerProductoCompleto(producto.getIdProducto()))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    @Override
    public GETProductoDTO obtenerProductoPorId(Integer id) {
        // VERIFICAR si el producto tiene precio activo antes de devolverlo
        if (!productoDAO.existsProductoWithPrecioActivo(id)) {
            throw new RuntimeException("Producto no disponible - sin precio activo");
        }

        return obtenerProductoCompleto(id);
    }

    // NUEVO MÉTODO: Para obtener cualquier producto (admin)
    public GETProductoDTO obtenerProductoPorIdAdmin(Integer id) {
        // Para uso administrativo - permite ver productos sin precio
        return obtenerProductoCompleto(id);
    }

    @Override
    @Transactional
    public GETProductoDTO crearProducto(CrearProductoDTO productoDTO) {
        try {
            // 1. Verificar que el empleado existe
            Empleado empleado = empleadoDAO.findById(productoDTO.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + productoDTO.getIdEmpleado()));

            // 2. Verificar nombre duplicado usando método optimizado
            if (productoDAO.existsByNomProductoIgnoreCase(productoDTO.getNomProducto().trim())) {
                throw new RuntimeException("Ya existe un producto con el nombre: " + productoDTO.getNomProducto());
            }

            // 3. Validar todos los insumos antes de crear
            for (CrearProductoDTO.InsumoDetalleDTO insumoDTO : productoDTO.getInsumos()) {
                if (insumoDTO.getCantPorInsumo() <= 0) {
                    throw new RuntimeException("La cantidad del insumo debe ser mayor a 0");
                }

                insumoDAO.findById(insumoDTO.getIdInsumo())
                        .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + insumoDTO.getIdInsumo()));

                tipoCantidadDAO.findById(insumoDTO.getIdTipoCantidad())
                        .orElseThrow(() -> new RuntimeException("Tipo de cantidad no encontrado con ID: " + insumoDTO.getIdTipoCantidad()));
            }

            // 4. Crear la entidad Producto
            Producto producto = Producto.builder()
                    .nomProducto(productoDTO.getNomProducto().trim())
                    .insumos(new ArrayList<>())
                    .precios(new ArrayList<>())
                    .build();

            // 5. GUARDAR PRODUCTO PRIMERO (para obtener el ID)
            Producto productoGuardado = productoDAO.save(producto);

            // 6. Crear y validar los ProductoInsumo
            List<ProductoInsumo> productosInsumos = new ArrayList<>();

            for (CrearProductoDTO.InsumoDetalleDTO insumoDTO : productoDTO.getInsumos()) {
                Insumo insumo = insumoDAO.findById(insumoDTO.getIdInsumo()).get();
                TipoCantidad tipoCantidad = tipoCantidadDAO.findById(insumoDTO.getIdTipoCantidad()).get();

                // Verificar que no se repita el mismo insumo
                boolean insumoYaExiste = productosInsumos.stream()
                        .anyMatch(pi -> pi.getInsumo().getIdInsumo().equals(insumoDTO.getIdInsumo()));

                if (insumoYaExiste) {
                    throw new RuntimeException("El insumo con ID " + insumoDTO.getIdInsumo() + " está duplicado");
                }

                ProductoInsumo productoInsumo = ProductoInsumo.builder()
                        .id(new ProductoInsumoPK(productoGuardado.getIdProducto(), insumo.getIdInsumo()))
                        .cantPorInsumo(insumoDTO.getCantPorInsumo())
                        .producto(productoGuardado)
                        .insumo(insumo)
                        .tipoCantidad(tipoCantidad)
                        .build();

                productosInsumos.add(productoInsumo);
            }

            // 7. Asignar los insumos al producto
            productoGuardado.setInsumos(productosInsumos);

            // 8. CREAR PRECIO INICIAL
            ProductoPrecio precioInicial = ProductoPrecio.builder()
                    .precio(productoDTO.getPrecioInicial())
                    .producto(productoGuardado)
                    .empleadoModifico(empleado)
                    .activo(true)
                    .build();

            productoPrecioDAO.save(precioInicial);

            // 9. Actualizar el producto con todas las relaciones
            productoGuardado = productoDAO.save(productoGuardado);

            // 10. Usar método que evita MultipleBagFetchException
            return obtenerProductoCompleto(productoGuardado.getIdProducto());

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno al crear producto: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public GETProductoDTO actualizarProducto(ActualizarProductoDTO productoDTO) {
        try {
            // 1. Cargar producto básico
            Producto producto = productoDAO.findByIdBasic(productoDTO.getIdProducto());
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + productoDTO.getIdProducto());
            }

            // 2. Verificar nombre duplicado
            if (!producto.getNomProducto().equalsIgnoreCase(productoDTO.getNomProducto())) {
                List<Producto> productosExistentes = productoDAO.findByNomProductoContainingIgnoreCase(productoDTO.getNomProducto());
                boolean nombreExiste = productosExistentes.stream()
                        .anyMatch(p -> !p.getIdProducto().equals(productoDTO.getIdProducto()) &&
                                p.getNomProducto().trim().equalsIgnoreCase(productoDTO.getNomProducto().trim()));

                if (nombreExiste) {
                    throw new RuntimeException("Ya existe otro producto con el nombre: " + productoDTO.getNomProducto());
                }
            }

            // 3. Actualizar nombre del producto
            producto.setNomProducto(productoDTO.getNomProducto().trim());

            // 4. Si se incluyen insumos, actualizar la receta
            if (productoDTO.getInsumos() != null && !productoDTO.getInsumos().isEmpty()) {
                // Cargar producto con insumos para limpiarlos
                Producto productoConInsumos = productoDAO.findByIdWithInsumos(productoDTO.getIdProducto());
                if (productoConInsumos != null) {
                    productoConInsumos.getInsumos().clear();

                    // Agregar nuevos insumos
                    List<ProductoInsumo> nuevosInsumos = new ArrayList<>();

                    for (CrearProductoDTO.InsumoDetalleDTO insumoDTO : productoDTO.getInsumos()) {
                        if (insumoDTO.getCantPorInsumo() <= 0) {
                            throw new RuntimeException("La cantidad del insumo debe ser mayor a 0");
                        }

                        Insumo insumo = insumoDAO.findById(insumoDTO.getIdInsumo())
                                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + insumoDTO.getIdInsumo()));

                        TipoCantidad tipoCantidad = tipoCantidadDAO.findById(insumoDTO.getIdTipoCantidad())
                                .orElseThrow(() -> new RuntimeException("Tipo de cantidad no encontrado con ID: " + insumoDTO.getIdTipoCantidad()));

                        ProductoInsumo productoInsumo = ProductoInsumo.builder()
                                .id(new ProductoInsumoPK(producto.getIdProducto(), insumo.getIdInsumo()))
                                .cantPorInsumo(insumoDTO.getCantPorInsumo())
                                .producto(producto)
                                .insumo(insumo)
                                .tipoCantidad(tipoCantidad)
                                .build();

                        nuevosInsumos.add(productoInsumo);
                    }

                    productoConInsumos.setInsumos(nuevosInsumos);
                    productoDAO.save(productoConInsumos);
                }
            }

            // 5. Guardar cambios
            productoDAO.save(producto);

            // 6. Usar método que evita MultipleBagFetchException
            return obtenerProductoCompleto(producto.getIdProducto());

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno al actualizar producto: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean eliminarProducto(Integer id) {
        try {
            Optional<Producto> productoOpt = productoDAO.findById(id);
            if (!productoOpt.isPresent()) {
                return false;
            }

            productoDAO.deleteById(id);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public List<GETProductoDTO> buscarProductosPorNombre(String nombre) {
        // YA FILTRA por precio activo en la query
        List<Producto> productos = productoDAO.findByNomProductoContainingIgnoreCase(nombre);
        return productos.stream()
                .map(producto -> obtenerProductoCompleto(producto.getIdProducto()))
                .filter(dto -> dto != null && dto.getPrecioActual() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<GETProductoDTO> buscarProductosPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        // YA FILTRA por precio activo en la query
        List<Producto> productos = productoDAO.findByRangoPrecio(precioMin, precioMax);
        return productos.stream()
                .map(producto -> obtenerProductoCompleto(producto.getIdProducto()))
                .filter(dto -> dto != null && dto.getPrecioActual() != null)
                .collect(Collectors.toList());
    }

    // ===================== GESTIÓN DE PRECIOS =====================

    @Override
    @Transactional
    public GETProductoDTO cambiarPrecio(CambiarPrecioDTO cambiarPrecioDTO) {
        try {
            Producto producto = productoDAO.findByIdBasic(cambiarPrecioDTO.getIdProducto());
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + cambiarPrecioDTO.getIdProducto());
            }

            Empleado empleado = empleadoDAO.findById(cambiarPrecioDTO.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + cambiarPrecioDTO.getIdEmpleado()));

            // Validar que el precio sea diferente al actual
            validarCambioPrecio(cambiarPrecioDTO.getIdProducto(), cambiarPrecioDTO.getNuevoPrecio());

            // Cerrar precio anterior
            LocalDateTime ahora = LocalDateTime.now();
            productoPrecioDAO.cerrarPrecioAnterior(cambiarPrecioDTO.getIdProducto(), ahora);

            // Crear nuevo precio
            ProductoPrecio nuevoPrecio = ProductoPrecio.builder()
                    .precio(cambiarPrecioDTO.getNuevoPrecio())
                    .producto(producto)
                    .empleadoModifico(empleado)
                    .activo(true)
                    .build();

            productoPrecioDAO.save(nuevoPrecio);

            return obtenerProductoCompleto(producto.getIdProducto());

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno al cambiar precio: " + e.getMessage(), e);
        }
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

    // ===================== MÉTODOS HELPER =====================

    // MÉTODO CLAVE: Obtener producto completo evitando MultipleBagFetchException
    private GETProductoDTO obtenerProductoCompleto(Integer id) {
        try {
            // 1. Cargar producto básico
            Producto producto = productoDAO.findByIdBasic(id);
            if (producto == null) {
                return null;
            }

            // 2. Crear DTO básico
            GETProductoDTO dto = new GETProductoDTO();
            dto.setIdProducto(producto.getIdProducto());
            dto.setNomProducto(producto.getNomProducto());

            // 3. Cargar precio actual por separado
            Optional<ProductoPrecio> precioActual = productoPrecioDAO.findPrecioActivoByProducto(id);
            if (precioActual.isPresent()) {
                ProductoPrecio precio = precioActual.get();
                dto.setPrecioActual(precio.getPrecio());
                dto.setFechaPrecioActual(precio.getFechaInicio());
                dto.setEmpleadoModificoPrecio(precio.getEmpleadoModifico().getNom_empleado());
            }

            // 4. Cargar insumos por separado
            Producto productoConInsumos = productoDAO.findByIdWithInsumos(id);
            if (productoConInsumos != null && productoConInsumos.getInsumos() != null) {
                List<GETProductoDTO.ProductoInsumoDTO> insumosDTO = productoConInsumos.getInsumos().stream()
                        .filter(pi -> pi.getInsumo() != null)
                        .map(this::convertirInsumoADTO)
                        .collect(Collectors.toList());
                dto.setInsumos(insumosDTO);
            }

            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener producto completo: " + e.getMessage(), e);
        }
    }

    // Método para validar precio duplicado
    private void validarCambioPrecio(Integer idProducto, BigDecimal nuevoPrecio) {
        BigDecimal precioActual = obtenerPrecioActual(idProducto);
        if (precioActual.compareTo(nuevoPrecio) == 0) {
            throw new RuntimeException("El nuevo precio es igual al precio actual");
        }
    }

    // Método helper separado para convertir insumos
    private GETProductoDTO.ProductoInsumoDTO convertirInsumoADTO(ProductoInsumo pi) {
        GETProductoDTO.ProductoInsumoDTO insumoDTO = new GETProductoDTO.ProductoInsumoDTO();
        insumoDTO.setIdInsumo(pi.getInsumo().getIdInsumo());
        insumoDTO.setNomInsumo(pi.getInsumo().getNomInsumo());
        insumoDTO.setCantPorInsumo(pi.getCantPorInsumo());

        if (pi.getTipoCantidad() != null) {
            insumoDTO.setNomTipoCantidad(pi.getTipoCantidad().getNomCantidad());
        }

        return insumoDTO;
    }
}