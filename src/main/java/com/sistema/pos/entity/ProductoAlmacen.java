package com.sistema.pos.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "producto_almacen")
public class ProductoAlmacen {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int stock;
    
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "almacen_id")
    @JsonIgnore
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    @Column(name = "ultima_modificacion")
    private LocalDateTime ultimaModificacion;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
    	ultimaModificacion = LocalDateTime.now(ZoneId.of("America/La_Paz"));
    }

}
