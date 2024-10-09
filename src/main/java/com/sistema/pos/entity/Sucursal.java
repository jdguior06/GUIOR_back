package com.sistema.pos.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "sucursal")
public class Sucursal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String nit;
    private String nombre;
    private String razonSocial;

    @OneToMany(mappedBy = "sucursal")
    private List<Inventario> inventarios;
}