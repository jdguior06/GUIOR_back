package com.sistema.pos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.DetalleVentaDTO;
import com.sistema.pos.dto.MetodoPagoDTO;
import com.sistema.pos.dto.VentaDTO;
import com.sistema.pos.entity.Almacen;
import com.sistema.pos.entity.CajaSesion;
import com.sistema.pos.entity.Cliente;
import com.sistema.pos.entity.DetalleVenta;
import com.sistema.pos.entity.EstadoVenta;
import com.sistema.pos.entity.MetodoPago;
import com.sistema.pos.entity.Producto;
import com.sistema.pos.entity.ProductoAlmacen;
import com.sistema.pos.entity.TipoPago;
import com.sistema.pos.entity.Venta;
import com.sistema.pos.repository.AlmacenRepository;
import com.sistema.pos.repository.ProductoAlmacenRepository;
import com.sistema.pos.repository.VentaRepository;
import com.sistema.pos.util.EmailService;
import com.sistema.pos.util.VentaPDFService;

import jakarta.transaction.Transactional;

@Service
public class VentaService {

	@Autowired
	private VentaRepository ventaRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private ProductoAlmacenRepository productoAlmacenRepository;
	
	@Autowired
	private AlmacenRepository almacenRepository;
	
	@Autowired CajaSesionService cajaSesionService;
	
	@Autowired
	private ProductoAlmacenService productoAlmacenService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private VentaPDFService ventaPDFService;

	@Transactional
	public List<Venta> listarVentas() {
		return ventaRepository.findAll();
	}
	
	public Double obtenerTotalVentasPorFechas(LocalDateTime startDate, LocalDateTime endDate) {
	    return ventaRepository.sumTotalVentasByFechaVentaBetween(startDate, endDate);
	}
	
	public List<Venta> obtenerVentasPorFechas(LocalDateTime startDate, LocalDateTime endDate) {
        return ventaRepository.findVentasByFechaVentaBetween(startDate, endDate);
    }
	
	public List<Venta> listarVentasPorSesionDeCaja(Long id) {
		return ventaRepository.findVentasByCajaSesion(id);
	}

	public Venta obtenerVenta(Long id) {
		Optional<Venta> venta = ventaRepository.findById(id);
		if (!venta.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la venta" + id);
		}
		return venta.get();
	}

	@Transactional
	@LoggableAction
	public Venta realizarVenta(VentaDTO ventaDTO) {
		
		CajaSesion cajaSesion = cajaSesionService.obtenerCajaSesion(ventaDTO.getId_caja_sesion());

		Cliente cliente = null;
	    if (ventaDTO.getId_cliente() != null) {
	        cliente = clienteService.obtenerClientePorId(ventaDTO.getId_cliente());
	    }
		
		Venta venta = new Venta();
		venta.setCliente(cliente);
		venta.setCajaSesion(cajaSesion);
		venta.setEstado(EstadoVenta.PAGADA);

		List<DetalleVenta> detalles = new ArrayList<>();
		Double total = 0.00;

		for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalleVentaDTOS()) {

			Producto producto = productoService.obtenerProducto(detalleDTO.getId_producto());
			
			List<Almacen> almacenes = almacenRepository.findBySucursal_IdAndActivoTrue(cajaSesion.getCaja().getSucursal().getId());
	        List<ProductoAlmacen> productosAlmacen = productoAlmacenRepository.findByProductoAndAlmacenIn(producto, almacenes);
	        
	        int stockTotal = productosAlmacen.stream().mapToInt(ProductoAlmacen::getStock).sum();
	        if (stockTotal < detalleDTO.getCantidad()) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                    "No hay suficiente stock total del producto: " + producto.getNombre());
	        }

	        int cantidadRestante = detalleDTO.getCantidad();
	        for (ProductoAlmacen productoAlmacen : productosAlmacen) {
	            if (cantidadRestante <= 0) break;

	            int disponible = productoAlmacen.getStock();
	            int aDescontar = Math.min(disponible, cantidadRestante);
	            productoAlmacen.setStock(disponible - aDescontar);
	            cantidadRestante -= aDescontar;

	            productoAlmacenService.actualizarStock(productoAlmacen);
	        }

			DetalleVenta detalleVenta = new DetalleVenta();
			detalleVenta.setProducto(producto);
			detalleVenta.setCantidad(detalleDTO.getCantidad());
			detalleVenta.setPrecio(producto.getPrecioVenta());
			detalleVenta.setMonto(producto.getPrecioVenta() * detalleDTO.getCantidad());
			detalleVenta.setVenta(venta);
			detalles.add(detalleVenta);
			total += detalleVenta.getMonto();
		}
		venta.setDetalleVentaList(detalles);
		venta.setTotal(total);
		
		List<MetodoPago> metodosPago = new ArrayList<>();
	    Double sumaPagos = 0.00;
	    Double montoEfectivo = 0.00;
	    for (MetodoPagoDTO metodoPagoDTO : ventaDTO.getMetodosPago()) {
	        MetodoPago metodoPago = new MetodoPago();
	        metodoPago.setVenta(venta);
	        metodoPago.setTipoPago(metodoPagoDTO.getTipoPago());
	        metodoPago.setMonto(metodoPagoDTO.getMonto());
	        metodoPago.setDetalles(metodoPagoDTO.getDetalles());

	        if (metodoPagoDTO.getTipoPago() == TipoPago.EFECTIVO) {
	        	montoEfectivo += metodoPagoDTO.getMonto(); 
	            metodoPago.setCambio(0.0);
	        } else {
	            metodoPago.setCambio(0.0);
	        }

	        sumaPagos += metodoPagoDTO.getMonto();
	        metodosPago.add(metodoPago);
	    }
	    
	    if (montoEfectivo > 0) {
	        Double cambio = montoEfectivo - total;
	        if (cambio < 0) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                    "El monto en efectivo no cubre el total de la venta.");
	        }

	        for (MetodoPago metodoPago : metodosPago) {
	            if (metodoPago.getTipoPago() == TipoPago.EFECTIVO) {
	                metodoPago.setCambio(cambio); 
	                break;
	            }
	        }
	        sumaPagos -= cambio; 
	    }

	    if (!sumaPagos.equals(total)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                "La suma de los métodos de pago no coincide con el total de la venta.");
	    }
	    venta.setMetodosPago(metodosPago);
	    
	    Venta ventaGuardada = ventaRepository.save(venta);
	    
	    if (venta.getCliente() != null && venta.getCliente().getEmail() != null && 
			    !venta.getCliente().getEmail().trim().isEmpty()) {
	        try {
	            byte[] pdf = ventaPDFService.generarFacturaPDF(venta);
	            emailService.enviarFacturaPorCorreo(venta.getCliente().getEmail(), pdf, "Factura_" + venta.getId() + ".pdf");
	        } catch (Exception e) {
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo", e);
	        }
	    }
	
		return ventaGuardada;
	}
	
	@Transactional
	@LoggableAction
	public Venta realizarPedido(VentaDTO ventaDTO) {
		
		CajaSesion cajaSesion = cajaSesionService.obtenerCajaSesion(ventaDTO.getId_caja_sesion());

		Cliente cliente = null;
	    if (ventaDTO.getId_cliente() != null) {
	        cliente = clienteService.obtenerClientePorId(ventaDTO.getId_cliente());
	    }
		
		Venta venta = new Venta();
		venta.setCliente(cliente);
		venta.setCajaSesion(cajaSesion);
		venta.setEstado(EstadoVenta.PENDIENTE);

		List<DetalleVenta> detalles = new ArrayList<>();
		Double total = 0.00;

		for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalleVentaDTOS()) {

			Producto producto = productoService.obtenerProducto(detalleDTO.getId_producto());
			
			List<Almacen> almacenes = almacenRepository.findBySucursal_IdAndActivoTrue(cajaSesion.getCaja().getSucursal().getId());
	        List<ProductoAlmacen> productosAlmacen = productoAlmacenRepository.findByProductoAndAlmacenIn(producto, almacenes);
	        
	        int stockTotal = productosAlmacen.stream().mapToInt(ProductoAlmacen::getStock).sum();
	        if (stockTotal < detalleDTO.getCantidad()) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                    "No hay suficiente stock total del producto: " + producto.getNombre());
	        }

			DetalleVenta detalleVenta = new DetalleVenta();
			detalleVenta.setProducto(producto);
			detalleVenta.setCantidad(detalleDTO.getCantidad());
			detalleVenta.setPrecio(producto.getPrecioVenta());
			detalleVenta.setMonto(producto.getPrecioVenta() * detalleDTO.getCantidad());
			detalleVenta.setVenta(venta);
			detalles.add(detalleVenta);
			total += detalleVenta.getMonto();
		}
		venta.setDetalleVentaList(detalles);
		venta.setTotal(total);
	    
	    Venta ventaGuardada = ventaRepository.save(venta);
	
		return ventaGuardada;
	}
	
	@Transactional
	@LoggableAction
	public Venta pagarPedido(Long idVenta, List<MetodoPagoDTO> metodoPagoDTO) {
		Venta venta = obtenerVenta(idVenta);
		if (venta.getEstado() != EstadoVenta.PENDIENTE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden pagar ventas pendientes");
			
		}
		
		for (DetalleVenta detalle : venta.getDetalleVentaList()) {
			Producto producto = detalle.getProducto();
			
			List<Almacen> almacenes = almacenRepository.findBySucursal_IdAndActivoTrue(venta.getCajaSesion().getCaja().getSucursal().getId());
	        List<ProductoAlmacen> productosAlmacen = productoAlmacenRepository.findByProductoAndAlmacenIn(producto, almacenes);
	        
	        int stockTotal = productosAlmacen.stream().mapToInt(ProductoAlmacen::getStock).sum();
	        if (stockTotal < detalle.getCantidad()) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                    "No hay suficiente stock total del producto: " + producto.getNombre());
	        }
	        
	        int cantidadRestante = detalle.getCantidad();
	        
	        for (ProductoAlmacen productoAlmacen : productosAlmacen) {
				if (cantidadRestante <= 0) {
					break;
				}
				int disponible = productoAlmacen.getStock();
				int aDescontar = Math.min(disponible, cantidadRestante);
				productoAlmacen.setStock(disponible - aDescontar);
				cantidadRestante -= aDescontar;
				productoAlmacenService.actualizarStock(productoAlmacen);
			}
			
	        if (cantidadRestante > 0) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                "Error al descontar stock. Stock insuficiente para: " + producto.getNombre());
	        }
			
		}
		
		List<MetodoPago> metodoPagos = new ArrayList<>();
		Double sumaPagos = 0.00;
		Double montoEfectivo = 0.00;
		
		for (MetodoPagoDTO metodoPagoDTO2 : metodoPagoDTO) {
			MetodoPago metodoPago = new MetodoPago();
			metodoPago.setVenta(venta);
			metodoPago.setTipoPago(metodoPagoDTO2.getTipoPago());
			metodoPago.setMonto(metodoPagoDTO2.getMonto());
			metodoPago.setDetalles(metodoPagoDTO2.getDetalles());
			
			if (metodoPagoDTO2.getTipoPago() == TipoPago.EFECTIVO) {
				montoEfectivo += metodoPagoDTO2.getMonto();
			}
			
			sumaPagos += metodoPagoDTO2.getMonto();
			metodoPagos.add(metodoPago);
		}
		
		if (montoEfectivo > 0) {
			Double cambio = montoEfectivo - venta.getTotal();
			if (cambio < 0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El efectivo no cubre el total.");
			}
			for (MetodoPago metodoPago : metodoPagos) {
				if (metodoPago.getTipoPago() == TipoPago.EFECTIVO) {
					metodoPago.setCambio(cambio);
					break;
				}
				
			}
			sumaPagos -= cambio;
		}
		
		if (!sumaPagos.equals(venta.getTotal())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"La suma de los métodos de pago no coinciden con el total de la venta");
		}
		
		venta.setMetodosPago(metodoPagos);
		venta.setEstado(EstadoVenta.PAGADA);
		
		Venta ventaGuardada = ventaRepository.save(venta);
		
		if (ventaGuardada.getCliente() != null && ventaGuardada.getCliente().getEmail() != null &&
				!ventaGuardada.getCliente().getEmail().trim().isEmpty()) {
			try {
				byte[] pdf = ventaPDFService.generarFacturaPDF(venta);
	            emailService.enviarFacturaPorCorreo(venta.getCliente().getEmail(), pdf, "Factura_" + venta.getId() + ".pdf");
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo", e);
			}
			
		}
		
		return ventaGuardada;
	}
	
	@Transactional
	@LoggableAction
	public Venta actualizarPedido(Long idVenta, VentaDTO ventaDTO) {
		Venta venta = obtenerVenta(idVenta);
		
		if (venta.getEstado() != EstadoVenta.PENDIENTE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden editar pedidos pendientes");
			
		}
		
		venta.getDetalleVentaList().clear();
		
		List<DetalleVenta> nuevosDetalles = new ArrayList<>();
		Double nuevoTotal = 0.00;
		
		for (DetalleVentaDTO detalleVentaDTO : ventaDTO.getDetalleVentaDTOS()) {
			Producto producto = productoService.obtenerProducto(detalleVentaDTO.getId_producto());
			DetalleVenta detalle = new DetalleVenta();
			detalle.setProducto(producto);
			detalle.setCantidad(detalleVentaDTO.getCantidad());
			detalle.setPrecio(producto.getPrecioVenta());
			detalle.setMonto(detalleVentaDTO.getCantidad() * producto.getPrecioVenta());
			detalle.setVenta(venta);
			
			nuevosDetalles.add(detalle);
			nuevoTotal += detalle.getMonto();
		}
		
		venta.setDetalleVentaList(nuevosDetalles);
		venta.setTotal(nuevoTotal);
		
		return ventaRepository.save(venta);
	}
	
	@Transactional
	@LoggableAction
	public Venta cancelarPedido(Long idVenta) {
		Venta venta = obtenerVenta(idVenta);
		
		if (venta.getEstado() != EstadoVenta.PENDIENTE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden cancelar los pedidos pendientes");
		}
		
		venta.setEstado(EstadoVenta.ANULADA);
		
		return ventaRepository.save(venta);
	}
	
}
