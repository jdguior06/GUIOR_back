package com.sistema.pos.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private String nombre;
    private Double precio;
    private String descripcion;
    private String foto;
    private Long id_categoria;


}
