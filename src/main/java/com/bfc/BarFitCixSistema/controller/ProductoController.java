package com.bfc.BarFitCixSistema.controller;

import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.ActualizarProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.CrearProductoDTO;
import com.bfc.BarFitCixSistema.model.DTO.ProductoDTO.GETProductoDTO;
import com.bfc.BarFitCixSistema.model.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/listar")
    public ResponseEntity<List<GETProductoDTO>> listarTodosProductos() {
        try {
            List<GETProductoDTO> productos = productoService.listarTodosProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GETProductoDTO> obtenerProductoPorId(@PathVariable Integer id) {
        try {
            GETProductoDTO producto = productoService.obtenerProductoPorId(id);
            if (producto != null) {
                return ResponseEntity.ok(producto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<GETProductoDTO> crearProducto(@RequestBody CrearProductoDTO productoDTO) {
        try {
            GETProductoDTO nuevoProducto = productoService.crearProducto(productoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<GETProductoDTO> actualizarProducto(@RequestBody ActualizarProductoDTO productoDTO) {
        try {
            GETProductoDTO productoActualizado = productoService.actualizarProducto(productoDTO);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        try {
            boolean eliminado = productoService.eliminarProducto(id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<GETProductoDTO>> buscarProductosPorNombre(@RequestParam String nombre) {
        try {
            List<GETProductoDTO> productos = productoService.buscarProductosPorNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<GETProductoDTO>> listarProductosActivos() {
        try {
            List<GETProductoDTO> productos = productoService.listarProductosActivos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<GETProductoDTO>> listarProductosInactivos() {
        try {
            List<GETProductoDTO> productos = productoService.listarProductosInactivos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}