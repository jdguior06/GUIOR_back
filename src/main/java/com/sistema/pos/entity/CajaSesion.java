package com.sistema.pos.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CajaSesion {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private LocalDateTime fechaHoraApertura; 
	
    private LocalDateTime fechaHoraCierre;   

    private Boolean abierta = false;
    
    private Double saldoInicial;
    
    private Double saldoFinal;
    
    private Double totalVentas;

    @ManyToOne
    @JoinColumn(name = "id_caja")
    private Caja caja;
    
    @ManyToOne
    @JoinColumn(name = "id_personal")
    private Usuario usuario;

    @OneToMany(mappedBy = "cajaSesion")
    @JsonIgnore
    private List<Venta> ventas;


}
