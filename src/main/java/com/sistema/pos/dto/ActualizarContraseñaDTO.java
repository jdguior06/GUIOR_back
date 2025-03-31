package com.sistema.pos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarContraseñaDTO {
	
	@NotBlank(message = "La contraseña actual no puede estar vacía")
    private String contraseñaActual;

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    private String nuevaContraseña;

    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String confirmarContraseña;

}
