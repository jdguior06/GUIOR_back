package com.sistema.pos.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Venta {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    
    private Double total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalleVentaList;
    
    private LocalDateTime fechaVenta;
    
    @ManyToOne
    @JoinColumn(name = "id_caja_sesion")
    private CajaSesion cajaSesion;
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<MetodoPago> metodosPago;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;
    
    @PrePersist
    public void prePersist() {
    	fechaVenta = LocalDateTime.now(ZoneId.of("America/La_Paz"));
    	if (estado == null) {
            estado = EstadoVenta.PENDIENTE;
        }
    }

}
