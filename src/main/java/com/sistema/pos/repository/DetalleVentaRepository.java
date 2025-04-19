package com.sistema.pos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema.pos.dto.ReporteProductoDTO;
import com.sistema.pos.entity.DetalleVenta;


@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
	
	@Query("SELECT new com.sistema.pos.dto.ReporteProductoDTO( " +
	           "d.producto.id, d.producto.nombre, SUM(d.cantidad),  d.producto.precioVenta, SUM(d.monto)) " +
	           "FROM DetalleVenta d " +
	           "JOIN d.venta v " +
	           "WHERE v.cajaSesion.id = :cajaSesionId AND v.estado = 'PAGADA'" +
	           "GROUP BY d.producto.id, d.producto.nombre, d.producto.precioVenta " +
	           "ORDER BY SUM(d.cantidad) DESC")
	 List<ReporteProductoDTO> obtenerReportePorCajaSesion(@Param("cajaSesionId") Long cajaSesionId);
	
	@Query("SELECT new com.sistema.pos.dto.ReporteProductoDTO( " +
	           "d.producto.id, d.producto.nombre, SUM(d.cantidad),  d.producto.precioVenta, SUM(d.monto)) " +
	           "FROM DetalleVenta d " +
	           "JOIN d.venta v " +
	           "WHERE v.fechaVenta BETWEEN :startDate AND :endDate AND v.estado = 'PAGADA'" +
	           "GROUP BY d.producto.id, d.producto.nombre, d.producto.precioVenta " +
	           "ORDER BY SUM(d.cantidad) DESC")
	 List<ReporteProductoDTO> obtenerReportePorFecha(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
