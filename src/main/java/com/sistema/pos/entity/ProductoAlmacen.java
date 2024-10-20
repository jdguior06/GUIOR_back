package com.sistema.pos.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "producto_almacen")
public class ProductoAlmacen {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int cantidad;
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen id_almacen;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto id_producto;

    /*@OneToMany(mappedBy = "id_producto_almacen", cascade = CascadeType.ALL)
    private List<OrdenProducto> id_orden_productos;
*/
}
