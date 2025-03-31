package com.sistema.pos.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sistema.pos.dto.ReporteProductoDTO;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.service.DetalleVentaService;
import com.sistema.pos.service.VentaService;
import com.sistema.pos.util.ReporteVentaService;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
	
	@Autowired
	private DetalleVentaService detalleVentaService;
	
	@Autowired
	private VentaService ventaService;
	
	@Autowired
	private ReporteVentaService reporteVentaService;
	
	@GetMapping("/productos/caja-sesion/{id}")
    public ResponseEntity<?> obtenerReporteProductosPorCajaSesion(@PathVariable("id") Long cajaSesionId) {
        try {
            List<ReporteProductoDTO> reporte = detalleVentaService.obtenerReporteProductosPorCajaSesion(cajaSesionId);
            if (reporte.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay ventas registradas en esta sesión de caja.");
            }
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al generar el reporte");
            errorResponse.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
	
	@GetMapping("/excel/caja-sesion/{id}")
    public ResponseEntity<byte[]> generarReportesCajaSesion(@PathVariable("id") Long cajaSesionId) {
        try {
            byte[] excelBytes = reporteVentaService.generarReporteProductosVendidosPorCajaSesionExcel(cajaSesionId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Reporte_Productos_Vendidos_por_Sesion_de_Caja.xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
        	e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/productos/por-fecha")
    public ResponseEntity<ApiResponse<Map<String, Object>>> reportesDeProdcutosVendidosPorFecha(
    		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicioFecha, 
    		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime finFecha ){
    	 
    	List<ReporteProductoDTO> reporte = detalleVentaService.obtenerReporteProductosPorFecha(inicioFecha, finFecha);
    	Double total = ventaService.obtenerTotalVentasPorFechas(inicioFecha, finFecha);
    	
    	Map<String, Object> response = new HashMap<>();
    	response.put("productos", reporte);
    	response.put("total", total);
    	
    	return ResponseEntity.ok(
    			ApiResponse.<Map<String, Object>>builder()
    			.statusCode(HttpStatus.OK.value())
    			.data(response)
    			.build()
    			);
    }
    
    @GetMapping("/productos/excel/por-fecha")
    public ResponseEntity<byte[]> generarReportesPorFecha(
    		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicioFecha, 
    		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime finFecha) {
        try {
            byte[] excelBytes = reporteVentaService.generarReporteProductosVendidosPorFechaExcel(inicioFecha, finFecha);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Reporte_Productos_Vendidos_por_Fecha.xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
        	e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
