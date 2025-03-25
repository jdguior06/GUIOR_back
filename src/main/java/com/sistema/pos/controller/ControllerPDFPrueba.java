package com.sistema.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.pos.entity.Venta;
import com.sistema.pos.service.VentaService;
import com.sistema.pos.util.EmailService;
import com.sistema.pos.util.ReporteVentaService;
import com.sistema.pos.util.VentaPDFService;

@RestController
@RequestMapping("/test")
public class ControllerPDFPrueba {
	
	@Autowired
    private VentaPDFService pdfService;
	
	@Autowired 
	private VentaService ventaService; 
	
	@Autowired 
	private ReporteVentaService reporteVentaService;
	
	
	@Autowired EmailService emailService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> generarPDFPrueba(@PathVariable Long id) throws Exception {
        Venta venta = ventaService.obtenerVenta(id);
        byte[] pdf = pdfService.generarFacturaPDF(venta);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "factura_prueba.pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
    
    @GetMapping("/enviar/{id}")
    public ResponseEntity<String> enviarCorreoDePrueba(@PathVariable Long id) {
        try {
        	Venta venta = ventaService.obtenerVenta(id);
            byte[] pdf = pdfService.generarFacturaPDF(venta);
            emailService.enviarFacturaPorCorreo("j.d.guior010602@gmail.com", pdf, "Factura_Prueba.pdf");
            return ResponseEntity.ok("Correo enviado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar correo: " + e.getMessage());
        }
    }
    
    @GetMapping("/excel")
    public ResponseEntity<byte[]> generarEXCELPrueba() {
        try {
            // Obtener lista de ventas
            List<Venta> ventas = ventaService.listarVentas();
            byte[] excelBytes = reporteVentaService.generarReporteVentasExcel(ventas);

            // Configurar headers para descargar el archivo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Reporte_Ventas.xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
        	e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
