package com.sistema.pos.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PedidoDTO {
    private String email;
    private String descripcion;
    private List<Pedido_ProductoDTO> detalle;



}