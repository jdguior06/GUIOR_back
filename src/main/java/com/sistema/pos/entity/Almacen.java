package com.sistema.pos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "almacenes")
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int numero;

    private String descripcion;

    @OneToOne()
    @JoinColumn(name = "id_sucursal")
    private Sucursal id_sucursal;

    @JsonIgnore
    @OneToMany(mappedBy = "id_almacen", cascade = CascadeType.ALL)
    private List<ProductoAlmacen> id_producto_almacen;

}
