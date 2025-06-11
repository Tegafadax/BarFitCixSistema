package com.bfc.BarFitCixSistema.controller;

import com.bfc.BarFitCixSistema.model.DAO.ProductoDAO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.*;
import com.bfc.BarFitCixSistema.model.entidad.Producto;
import com.bfc.BarFitCixSistema.model.service.ProductoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoDAO productoDAO;

    // ===================== CRUD BÁSICO =====================

    @GetMapping("/listar")
    public ResponseEntity<?> listarTodosProductos() {
        try {
            List<GETProductoDTO> productos = productoService.listarTodosProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            log.error("Error al listar productos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }
    // 3. OBTENER PRODUCTO POR ID (HELPER PARA FRONTEND)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            GETProductoDTO producto = productoService.obtenerProductoPorId(id);
            if (producto != null) {
                log.info("Producto obtenido: ID {}", id);
                return ResponseEntity.ok(producto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Producto no encontrado", "mensaje", "No existe producto con ID: " + id));
            }
        } catch (Exception e) {
            log.error("Error al obtener producto con ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody CrearProductoDTO productoDTO) {
        try {
            // Validaciones adicionales
            if (productoDTO.getPrecioInicial().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Precio inválido", "mensaje", "El precio debe ser mayor a 0"));
            }

            if (productoDTO.getInsumos() == null || productoDTO.getInsumos().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Insumos requeridos", "mensaje", "El producto debe tener al menos un insumo"));
            }

            GETProductoDTO nuevoProducto = productoService.crearProducto(productoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);

        } catch (RuntimeException e) {
            log.warn("Error de validación al crear producto: ", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error de validación", "mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al crear producto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarProducto(@Valid @RequestBody ActualizarProductoDTO productoDTO) {
        try {
            if (productoDTO.getIdProducto() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            GETProductoDTO productoActualizado = productoService.actualizarProducto(productoDTO);
            return ResponseEntity.ok(productoActualizado);

        } catch (RuntimeException e) {
            log.warn("Error de validación al actualizar producto: ", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error de validación", "mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al actualizar producto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            boolean eliminado = productoService.eliminarProducto(id);
            if (eliminado) {
                return ResponseEntity.ok()
                        .body(Map.of("mensaje", "Producto eliminado exitosamente", "id", id));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Producto no encontrado", "mensaje", "No existe producto con ID: " + id));
            }
        } catch (Exception e) {
            log.error("Error al eliminar producto con ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    // ===================== BÚSQUEDAS =====================

    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<?> buscarProductosPorNombre(@RequestParam String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Parámetro inválido", "mensaje", "El nombre no puede estar vacío"));
            }

            List<GETProductoDTO> productos = productoService.buscarProductosPorNombre(nombre.trim());
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            log.error("Error al buscar productos por nombre '{}': ", nombre, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/buscar-por-precio")
    public ResponseEntity<?> buscarProductosPorRangoPrecio(
            @RequestParam BigDecimal precioMin,
            @RequestParam BigDecimal precioMax) {
        try {
            // Validaciones
            if (precioMin.compareTo(BigDecimal.ZERO) < 0 || precioMax.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Precios inválidos", "mensaje", "Los precios no pueden ser negativos"));
            }

            if (precioMin.compareTo(precioMax) > 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Rango inválido", "mensaje", "El precio mínimo no puede ser mayor al máximo"));
            }

            List<GETProductoDTO> productos = productoService.buscarProductosPorRangoPrecio(precioMin, precioMax);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            log.error("Error al buscar productos por rango de precio [{} - {}]: ", precioMin, precioMax, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    // ===================== GESTIÓN DE PRECIOS =====================

    @PostMapping("/cambiar-precio")
    public ResponseEntity<?> cambiarPrecio(@Valid @RequestBody CambiarPrecioDTO cambiarPrecioDTO) {
        try {
            // Validaciones
            if (cambiarPrecioDTO.getNuevoPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Precio inválido", "mensaje", "El nuevo precio debe ser mayor a 0"));
            }

            if (cambiarPrecioDTO.getIdProducto() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID del producto debe ser mayor a 0"));
            }

            if (cambiarPrecioDTO.getIdEmpleado() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID del empleado debe ser mayor a 0"));
            }

            GETProductoDTO productoActualizado = productoService.cambiarPrecio(cambiarPrecioDTO);
            return ResponseEntity.ok(productoActualizado);

        } catch (RuntimeException e) {
            log.warn("Error de validación al cambiar precio: ", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error de validación", "mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al cambiar precio: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/{id}/historial-precios")
    public ResponseEntity<?> obtenerHistorialPrecios(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            List<HistorialPrecioDTO> historial = productoService.obtenerHistorialPrecios(id);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            log.error("Error al obtener historial de precios para producto {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/{id}/precio-actual")
    public ResponseEntity<?> obtenerPrecioActual(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            BigDecimal precio = productoService.obtenerPrecioActual(id);
            return ResponseEntity.ok(Map.of("idProducto", id, "precioActual", precio));
        } catch (Exception e) {
            log.error("Error al obtener precio actual para producto {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    // ===================== ENDPOINTS NUEVOS - ADMINISTRATIVOS =====================

    @GetMapping("/admin-listar-todos")
    public ResponseEntity<?> listarTodosProductosAdmin() {
        try {
            // Usar el método DAO directo para obtener TODOS los productos
            List<Producto> todosProductos = productoDAO.findAllProductos();

            List<GETProductoDTO> productos = todosProductos.stream()
                    .map(producto -> productoService.obtenerProductoPorId(producto.getIdProducto()))
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "productos", productos,
                    "total", productos.size(),
                    "mensaje", "Incluye productos sin precio activo"
            ));
        } catch (Exception e) {
            log.error("Error al listar todos los productos (admin): ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> obtenerProductoPorIdAdmin(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            // Usar el método normal pero sin validar precio activo
            Producto producto = productoDAO.findByIdBasic(id);
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Producto no encontrado", "mensaje", "No existe producto con ID: " + id));
            }

            // Usar el método actual del service pero permitir productos sin precio activo
            GETProductoDTO productoDTO = productoService.obtenerProductoPorId(id);
            return ResponseEntity.ok(productoDTO);

        } catch (Exception e) {
            log.error("Error al obtener producto admin con ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    // ===================== ENDPOINTS MEJORADOS =====================

    @GetMapping("/disponibles")
    public ResponseEntity<?> listarProductosDisponibles() {
        try {
            // Usar query optimizada que ya filtra por precio activo
            List<Producto> productos = productoDAO.findProductosDisponibles();

            List<GETProductoDTO> disponibles = productos.stream()
                    .map(producto -> productoService.obtenerProductoPorId(producto.getIdProducto()))
                    .filter(dto -> dto != null && dto.getPrecioActual() != null)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "productos", disponibles,
                    "total", disponibles.size(),
                    "mensaje", "Solo productos con precio activo mayor a 0"
            ));
        } catch (Exception e) {
            log.error("Error al listar productos disponibles: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/{id}/disponible")
    public ResponseEntity<?> verificarDisponibilidad(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            boolean disponible = productoDAO.existsProductoWithPrecioActivo(id);
            BigDecimal precio = disponible ? productoService.obtenerPrecioActual(id) : BigDecimal.ZERO;

            return ResponseEntity.ok(Map.of(
                    "idProducto", id,
                    "disponible", disponible,
                    "precioActual", precio,
                    "mensaje", disponible ? "Producto disponible" : "Producto sin precio activo"
            ));
        } catch (Exception e) {
            log.error("Error al verificar disponibilidad para producto {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticasProductos() {
        try {
            // Obtener todos los productos (admin)
            List<Producto> todosProductos = productoDAO.findAllProductos();
            // Obtener solo productos con precio activo (normal)
            List<GETProductoDTO> productosConPrecio = productoService.listarTodosProductos();

            long totalProductos = todosProductos.size();
            long productosConPrecioActivo = productosConPrecio.size();
            long productosSinPrecio = totalProductos - productosConPrecioActivo;

            double precioPromedio = productosConPrecio.stream()
                    .filter(p -> p.getPrecioActual() != null)
                    .mapToDouble(p -> p.getPrecioActual().doubleValue())
                    .average()
                    .orElse(0.0);

            Map<String, Object> estadisticas = Map.of(
                    "totalProductos", totalProductos,
                    "productosConPrecioActivo", productosConPrecioActivo,
                    "productosSinPrecioActivo", productosSinPrecio,
                    "porcentajeDisponibles", totalProductos > 0 ? (productosConPrecioActivo * 100.0 / totalProductos) : 0,
                    "precioPromedio", Math.round(precioPromedio * 100.0) / 100.0
            );

            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de productos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    // 1. ACTUALIZAR SOLO EL NOMBRE DEL PRODUCTO
    @PutMapping("/actualizar-nombre")
    public ResponseEntity<?> actualizarNombreProducto(@Valid @RequestBody ActualizarNombreDTO nombreDTO) {
        try {
            if (nombreDTO.getIdProducto() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            if (nombreDTO.getNomProducto() == null || nombreDTO.getNomProducto().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Nombre requerido", "mensaje", "El nombre del producto no puede estar vacío"));
            }

            // Crear DTO compatible con el método existente
            ActualizarProductoDTO productoDTO = new ActualizarProductoDTO();
            productoDTO.setIdProducto(nombreDTO.getIdProducto());
            productoDTO.setNomProducto(nombreDTO.getNomProducto().trim());
            // No incluir insumos para que solo actualice el nombre

            GETProductoDTO productoActualizado = productoService.actualizarProducto(productoDTO);
            log.info("Nombre actualizado para producto ID {}: {}", nombreDTO.getIdProducto(), nombreDTO.getNomProducto());

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Nombre del producto actualizado exitosamente",
                    "producto", productoActualizado
            ));

        } catch (RuntimeException e) {
            log.warn("Error al actualizar nombre del producto: ", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error de validación", "mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al actualizar nombre: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    // 2. ACTUALIZAR SOLO LOS INSUMOS (RECETA)
    // ENDPOINT CORREGIDO EN EL CONTROLLER
    @PutMapping("/actualizar-insumos")
    public ResponseEntity<?> actualizarInsumosProducto(@Valid @RequestBody ActualizarInsumosDTO insumosDTO) {
        try {
            if (insumosDTO.getIdProducto() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID inválido", "mensaje", "El ID debe ser mayor a 0"));
            }

            if (insumosDTO.getInsumos() == null || insumosDTO.getInsumos().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Insumos requeridos", "mensaje", "Debe especificar al menos un insumo"));
            }

            // Validar cada insumo
            for (CrearProductoDTO.InsumoDetalleDTO insumo : insumosDTO.getInsumos()) {
                if (insumo.getCantPorInsumo() <= 0) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Cantidad inválida", "mensaje", "La cantidad debe ser mayor a 0"));
                }
            }

            // USAR EL MÉTODO DIRECTO EN LUGAR DEL GENÉRICO
            GETProductoDTO productoActualizado = productoService.actualizarInsumosDirecto(
                    insumosDTO.getIdProducto(),
                    insumosDTO.getInsumos()
            );

            log.info("Insumos actualizados para producto ID {}: {} insumos",
                    insumosDTO.getIdProducto(), insumosDTO.getInsumos().size());

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Receta del producto actualizada exitosamente",
                    "producto", productoActualizado,
                    "insumosActualizados", insumosDTO.getInsumos().size()
            ));

        } catch (RuntimeException e) {
            log.warn("Error al actualizar insumos del producto: ", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error de validación", "mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al actualizar insumos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }



}