package com.sistema.pos.service;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.ProductoConsolidadoDTO;
import com.sistema.pos.entity.Almacen;
import com.sistema.pos.entity.Producto;
import com.sistema.pos.entity.ProductoAlmacen;
import com.sistema.pos.repository.AlmacenRepository;
import com.sistema.pos.repository.ProductoAlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoAlmacenService {
	
    @Autowired
    ProductoAlmacenRepository productoAlmacenRepository;
    
    @Autowired
    private AlmacenService almacenService;
    
    @Autowired
    private AlmacenRepository almacenRepository;
    
    @Autowired
    private ProductoService productoService;

    public List<ProductoAlmacen> findAll() {
        return productoAlmacenRepository.findAll();
    }

    public List<ProductoAlmacen> findAllByAlmacenId(Long idAlmacen) {
        List<ProductoAlmacen> productosAlmacen = productoAlmacenRepository.findByAlmacen_Id(idAlmacen);
        return productosAlmacen;
    }


    public boolean existe(Long id_almacen, Long id_producto){
        Long cantidad = productoAlmacenRepository.verificarProducto(id_almacen,id_producto);
        return cantidad != null && cantidad > 0; 
    }

    public ProductoAlmacen obtener(Long id){
        Optional<ProductoAlmacen> productoA = productoAlmacenRepository.findById(id);
        if(!productoA.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto con el id" + id);

        }return productoA.get();
    }
    
    public ProductoAlmacen obtenerProductoAlmacenPorProductoYAlmacen(Long almacenId, Long productoId) {
        return productoAlmacenRepository.findByAlmacen_IdAndProducto_Id(almacenId, productoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Producto no encontrado en el almacén seleccionado"));
    }

    @LoggableAction
    public ProductoAlmacen actualizarOGuardarStock(Long almacenId, Long productoId, int cantidad) {
        Optional<ProductoAlmacen> existente = productoAlmacenRepository.findByAlmacen_IdAndProducto_Id(almacenId, productoId);

        if (existente.isPresent()) {
            ProductoAlmacen productoExistente = existente.get();
            productoExistente.setStock(productoExistente.getStock() + cantidad);
            return productoAlmacenRepository.save(productoExistente);
        } else {
            Producto producto = productoService.obtenerProducto(productoId);
            Almacen almacen = almacenService.obtenerAlmacenId(almacenId);
            ProductoAlmacen nuevoProductoAlmacen = new ProductoAlmacen();
            nuevoProductoAlmacen.setProducto(producto);
            nuevoProductoAlmacen.setAlmacen(almacen);
            nuevoProductoAlmacen.setStock(cantidad);
            nuevoProductoAlmacen.setActivo(true);
            return productoAlmacenRepository.save(nuevoProductoAlmacen);
        }
    }

    @LoggableAction
    public ProductoAlmacen eliminar(Long id) {
        ProductoAlmacen productoAlmacen =obtener(id);
        productoAlmacen.setActivo(false);
        return productoAlmacenRepository.save(productoAlmacen);
    }
    
    public Optional<ProductoAlmacen> buscarAlmacenConStock(Long idProducto, Integer cantidad) {
        return productoAlmacenRepository.findByProductoIdAndStockGreaterThanEqualOrderByStockDesc(idProducto, cantidad);
    }
    
    public void actualizarStock(ProductoAlmacen productoAlmacen) {
    	productoAlmacenRepository.save(productoAlmacen);
    }
    
    public List<ProductoConsolidadoDTO> listarProductosConsolidadoPorSucursal(Long idSucursal) {
    	
        List<Almacen> almacenes = almacenRepository.findBySucursalId(idSucursal);

        List<ProductoAlmacen> productosAlmacen = productoAlmacenRepository.findByAlmacenIn(almacenes);

        Map<Long, ProductoConsolidadoDTO> productoMap = new HashMap<>();
        for (ProductoAlmacen pa : productosAlmacen) {
            Long idProducto = pa.getProducto().getId();
            ProductoConsolidadoDTO dto = productoMap.getOrDefault(
                idProducto,
                new ProductoConsolidadoDTO(pa.getProducto(), 0) 
            );

            dto.setTotalStock(dto.getTotalStock() + pa.getStock());
            productoMap.put(idProducto, dto);
        }

        return new ArrayList<>(productoMap.values());
    }
    
}
