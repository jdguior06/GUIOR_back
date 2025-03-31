package com.sistema.pos.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.pos.dto.ReporteProductoDTO;
import com.sistema.pos.entity.CajaSesion;
import com.sistema.pos.entity.Venta;
import com.sistema.pos.service.CajaSesionService;
import com.sistema.pos.service.DetalleVentaService;
import com.sistema.pos.service.VentaService;

@Service
public class ReporteVentaService {
	
	@Autowired
	private DetalleVentaService detalleVentaService;
	
	@Autowired
	private VentaService ventaService;
	
	@Autowired 
	private CajaSesionService cajaSesionService;

	public byte[] generarReporteVentasExcel(List<Venta> ventas) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Ventas");

			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);

			CellStyle borderedStyle = workbook.createCellStyle();
			borderedStyle.setBorderTop(BorderStyle.THIN);
			borderedStyle.setBorderBottom(BorderStyle.THIN);
			borderedStyle.setBorderLeft(BorderStyle.THIN);
			borderedStyle.setBorderRight(BorderStyle.THIN);

			CellStyle currencyStyle = workbook.createCellStyle();
			currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			currencyStyle.setBorderTop(BorderStyle.THIN);
			currencyStyle.setBorderBottom(BorderStyle.THIN);
			currencyStyle.setBorderLeft(BorderStyle.THIN);
			currencyStyle.setBorderRight(BorderStyle.THIN);

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			Row titleRow = sheet.createRow(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("Reporte de Ventas");
			titleCell.setCellStyle(titleStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

			Row header = sheet.createRow(1);
			String[] columns = { "ID", "Cliente", "Total", "Fecha Venta" };
			for (int i = 0; i < columns.length; i++) {
				Cell cell = header.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			double total = 0.00;
			int rowIndex = 2;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

			for (Venta venta : ventas) {
				Row row = sheet.createRow(rowIndex++);
				Cell idCell = row.createCell(0);
				idCell.setCellValue(venta.getId());
				idCell.setCellStyle(borderedStyle);

				String nombreCliente = (venta.getCliente() != null) ? venta.getCliente().getNombre()
						: "Cliente no registrado";
				Cell clienteCell = row.createCell(1);
				clienteCell.setCellValue(nombreCliente);
				clienteCell.setCellStyle(borderedStyle);

				Cell totalCell = row.createCell(2);
				totalCell.setCellValue(venta.getTotal());
				totalCell.setCellStyle(currencyStyle);

				Cell dateCell = row.createCell(3);
				dateCell.setCellValue(venta.getFechaVenta().format(formatter));
				dateCell.setCellStyle(dateStyle);

				total += venta.getTotal();
			}

			Row totalRow = sheet.createRow(rowIndex);
			Cell totalLabelCell = totalRow.createCell(1);
			totalLabelCell.setCellValue("Total:");
			totalLabelCell.setCellStyle(headerStyle);

			Cell totalAmountCell = totalRow.createCell(2);
			totalAmountCell.setCellValue(total);
			totalAmountCell.setCellStyle(currencyStyle);

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			return out.toByteArray();
		}
	}
	
	
	public byte[] generarReporteProductosVendidosPorCajaSesionExcel(Long cajaSesionId) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			
			List<ReporteProductoDTO> reportes = detalleVentaService.obtenerReporteProductosPorCajaSesion(cajaSesionId);
			
			CajaSesion cajaSesion = cajaSesionService.obtenerCajaSesion(cajaSesionId);
			
			Sheet sheet = workbook.createSheet("Ventas_Productos");

			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);

			CellStyle borderedStyle = workbook.createCellStyle();
			borderedStyle.setBorderTop(BorderStyle.THIN);
			borderedStyle.setBorderBottom(BorderStyle.THIN);
			borderedStyle.setBorderLeft(BorderStyle.THIN);
			borderedStyle.setBorderRight(BorderStyle.THIN);

			CellStyle currencyStyle = workbook.createCellStyle();
			currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			currencyStyle.setBorderTop(BorderStyle.THIN);
			currencyStyle.setBorderBottom(BorderStyle.THIN);
			currencyStyle.setBorderLeft(BorderStyle.THIN);
			currencyStyle.setBorderRight(BorderStyle.THIN);

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			Row titleRow = sheet.createRow(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("Reporte de Productos Vendidos por Sesion de Caja");
			titleCell.setCellStyle(titleStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			Row title1Row = sheet.createRow(2);
			Cell title1Cell = title1Row.createCell(0);
			title1Cell.setCellValue("Caja: ");
			title1Cell.setCellStyle(headerStyle);
			Cell body1Cell = title1Row.createCell(1);
			body1Cell.setCellValue(cajaSesion.getCaja().getNombre());
			body1Cell.setCellStyle(borderedStyle);
			
			Cell title2Cell = title1Row.createCell(3);
			title2Cell.setCellValue("Usuario: ");
			title2Cell.setCellStyle(headerStyle);
			Cell body2Cell = title1Row.createCell(4);
			body2Cell.setCellValue(cajaSesion.getUsuario().getNombre());
			body2Cell.setCellStyle(borderedStyle);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			
			Row title2Row = sheet.createRow(3);
			Cell title3Cell = title2Row.createCell(0);
			title3Cell.setCellValue("Fecha de Apertura: ");
			title3Cell.setCellStyle(headerStyle);
			Cell body3Cell = title2Row.createCell(1);
			body3Cell.setCellValue(cajaSesion.getFechaHoraApertura().format(formatter));
			body3Cell.setCellStyle(dateStyle);
			
			Cell title4Cell = title2Row.createCell(3);
			title4Cell.setCellValue("Fecha de Cierre: ");
			title4Cell.setCellStyle(headerStyle);
			Cell body4Cell = title2Row.createCell(4);
			body4Cell.setCellValue(cajaSesion.getFechaHoraCierre().format(formatter));
			body4Cell.setCellStyle(dateStyle);
			
			Row title3Row = sheet.createRow(4);
			Cell title5Cell = title3Row.createCell(0);
			title5Cell.setCellValue("Monto Inicial: ");
			title5Cell.setCellStyle(headerStyle);
			Cell body5Cell = title3Row.createCell(1);
			body5Cell.setCellValue(cajaSesion.getSaldoInicial());
			body5Cell.setCellStyle(currencyStyle);
			
			Cell title6Cell = title3Row.createCell(3);
			title6Cell.setCellValue("Monto Final: ");
			title6Cell.setCellStyle(headerStyle);
			Cell body6Cell = title3Row.createCell(4);
			body6Cell.setCellValue(cajaSesion.getSaldoFinal());
			body6Cell.setCellStyle(currencyStyle);

			Row header = sheet.createRow(6);
			String[] columns = { "ID", "Producto", "Cantidad Vendida", "Precio Unitario", "Subtotal" };
			for (int i = 0; i < columns.length; i++) {
				Cell cell = header.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			double total = 0.00;
			int rowIndex = 7;

			for (ReporteProductoDTO reporte : reportes) {
				Row row = sheet.createRow(rowIndex++);
				Cell idCell = row.createCell(0);
				idCell.setCellValue(reporte.getIdProducto());
				idCell.setCellStyle(borderedStyle);

				Cell productoCell = row.createCell(1);
				productoCell.setCellValue(reporte.getNombreProducto());
				productoCell.setCellStyle(borderedStyle);

				Cell cantidadCell = row.createCell(2);
				cantidadCell.setCellValue(reporte.getCantidadTotal());
				cantidadCell.setCellStyle(borderedStyle);
				
				Cell precioCell = row.createCell(3);
				precioCell.setCellValue(reporte.getPrecioUnitario());
				precioCell.setCellStyle(currencyStyle);

				Cell dateCell = row.createCell(4);
				dateCell.setCellValue(reporte.getPrecioTotal());
				dateCell.setCellStyle(currencyStyle);

				total += reporte.getPrecioTotal();
			}

			Row totalRow = sheet.createRow(rowIndex);
			Cell totalLabelCell = totalRow.createCell(3);
			totalLabelCell.setCellValue("Total:");
			totalLabelCell.setCellStyle(headerStyle);

			Cell totalAmountCell = totalRow.createCell(4);
			totalAmountCell.setCellValue(total);
			totalAmountCell.setCellStyle(currencyStyle);

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			return out.toByteArray();
		}
	}
	
	public byte[] generarReporteProductosVendidosPorFechaExcel(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			
			List<ReporteProductoDTO> reportes = detalleVentaService.obtenerReporteProductosPorFecha(fechaInicio, fechaFin);
			Double total = ventaService.obtenerTotalVentasPorFechas(fechaInicio, fechaFin);
			
			Sheet sheet = workbook.createSheet("Ventas_Productos_Fecha");

			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);

			CellStyle borderedStyle = workbook.createCellStyle();
			borderedStyle.setBorderTop(BorderStyle.THIN);
			borderedStyle.setBorderBottom(BorderStyle.THIN);
			borderedStyle.setBorderLeft(BorderStyle.THIN);
			borderedStyle.setBorderRight(BorderStyle.THIN);

			CellStyle currencyStyle = workbook.createCellStyle();
			currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			currencyStyle.setBorderTop(BorderStyle.THIN);
			currencyStyle.setBorderBottom(BorderStyle.THIN);
			currencyStyle.setBorderLeft(BorderStyle.THIN);
			currencyStyle.setBorderRight(BorderStyle.THIN);

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			Row titleRow = sheet.createRow(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("Reporte de Productos Vendidos por fecha");
			titleCell.setCellStyle(titleStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			
			Row title2Row = sheet.createRow(2);
			Cell title3Cell = title2Row.createCell(0);
			title3Cell.setCellValue("Fecha inicial: ");
			title3Cell.setCellStyle(headerStyle);
			Cell body3Cell = title2Row.createCell(1);
			body3Cell.setCellValue(fechaInicio.format(formatter));
			body3Cell.setCellStyle(dateStyle);
			
			Cell title4Cell = title2Row.createCell(3);
			title4Cell.setCellValue("Fecha final: ");
			title4Cell.setCellStyle(headerStyle);
			Cell body4Cell = title2Row.createCell(4);
			body4Cell.setCellValue(fechaFin.format(formatter));
			body4Cell.setCellStyle(dateStyle);
			

			Row header = sheet.createRow(4);
			String[] columns = { "ID", "Producto", "Cantidad Vendida", "Precio Unitario", "Subtotal" };
			for (int i = 0; i < columns.length; i++) {
				Cell cell = header.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			int rowIndex = 5;

			for (ReporteProductoDTO reporte : reportes) {
				Row row = sheet.createRow(rowIndex++);
				Cell idCell = row.createCell(0);
				idCell.setCellValue(reporte.getIdProducto());
				idCell.setCellStyle(borderedStyle);

				Cell productoCell = row.createCell(1);
				productoCell.setCellValue(reporte.getNombreProducto());
				productoCell.setCellStyle(borderedStyle);

				Cell cantidadCell = row.createCell(2);
				cantidadCell.setCellValue(reporte.getCantidadTotal());
				cantidadCell.setCellStyle(borderedStyle);
				
				Cell precioCell = row.createCell(3);
				precioCell.setCellValue(reporte.getPrecioUnitario());
				precioCell.setCellStyle(currencyStyle);

				Cell dateCell = row.createCell(4);
				dateCell.setCellValue(reporte.getPrecioTotal());
				dateCell.setCellStyle(currencyStyle);

			}

			Row totalRow = sheet.createRow(rowIndex);
			Cell totalLabelCell = totalRow.createCell(3);
			totalLabelCell.setCellValue("Total:");
			totalLabelCell.setCellStyle(headerStyle);

			Cell totalAmountCell = totalRow.createCell(4);
			totalAmountCell.setCellValue(total);
			totalAmountCell.setCellStyle(currencyStyle);

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			return out.toByteArray();
		}
	}
	
}
