package com.sistema.pos.entity;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rol")
@Entity
public class Rol implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nombre;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
    		name = "rol_permiso",
    		joinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"),
    		inverseJoinColumns =  @JoinColumn(name = "permiso_id", referencedColumnName = "id")
    		)
    private Set<Permiso> permiso;

	public Rol(String nombre) {
		super();
		this.nombre = nombre;
	}

}
