package com.sistema.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteProductoDTO {
	
	private Long idProducto;
    private String nombreProducto;
    private Integer cantidadTotal;
    private float precioUnitario;
    private Double precioTotal;
    
    public ReporteProductoDTO(Long idProducto, String nombreProducto, Long cantidadTotal, float precioUnitario, Double precioTotal) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadTotal = cantidadTotal.intValue(); // Convertir de Long a Integer si es necesario
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
    }

}
