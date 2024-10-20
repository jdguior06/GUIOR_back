package com.sistema.pos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proveedor", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Proveedor {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
   
    private String nombre;
    
    private String telefono;
    
    private String direccion;
    
    @Email(message = "El email debe ser válido")
    private String email;
    
    private boolean activo;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota_Entrada> notasEntrada;
}
