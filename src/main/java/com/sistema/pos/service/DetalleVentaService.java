package com.sistema.pos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.pos.dto.ReporteProductoDTO;
import com.sistema.pos.repository.DetalleVentaRepository;

@Service
public class DetalleVentaService {
	
	@Autowired
	private DetalleVentaRepository detalleVentaRepository;
	
	public List<ReporteProductoDTO> obtenerReporteProductosPorCajaSesion(Long cajaSesionId) {
        return detalleVentaRepository.obtenerReportePorCajaSesion(cajaSesionId);
    }
	
	public List<ReporteProductoDTO> obtenerReporteProductosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		return detalleVentaRepository.obtenerReportePorFecha(fechaInicio, fechaFin);
	}
	
	

}
