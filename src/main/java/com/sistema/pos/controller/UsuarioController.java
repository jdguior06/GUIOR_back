package com.sistema.pos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.pos.dto.ActualizarContrasenaDTO;
import com.sistema.pos.dto.ThemeColorRequest;
import com.sistema.pos.dto.UsuarioDTO;
import com.sistema.pos.entity.Usuario;
import com.sistema.pos.response.ApiResponse;
import com.sistema.pos.response.AuthResponse;
import com.sistema.pos.service.UsuarioService;
import com.sistema.pos.util.HttpStatusMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UsuarioController {
	
	@Autowired
    private UsuarioService service;

	@GetMapping
	@PreAuthorize("hasAuthority('PERMISO_GESTIONAR_PERSONAL')")
	public ResponseEntity<ApiResponse<List<Usuario>>> listarUsuarios(@RequestParam(value = "search", required = false) String searchTerm) {
		List<Usuario> user;
		if (searchTerm != null && !searchTerm.isEmpty()) {
			user = service.buscarUsuarios(searchTerm);
        } else {
        	user = service.listUsuario();
        }
		return new ResponseEntity<>(
				ApiResponse.<List<Usuario>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(user)
						.build(),
				HttpStatus.OK
		);
	}
	
    @GetMapping("/protected")
    public ResponseEntity<String> checkAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities()); 
        
        return ResponseEntity.ok("Acceso concedido");
    }
    
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(service.loader(authentication));
    }
    
    @PutMapping("/theme")
    @PreAuthorize("isAuthenticated()")
    public void updateThemeColor(Authentication authentication, @RequestBody ThemeColorRequest themeColorRequest) {
    	service.updateThemeColor(authentication, themeColorRequest.getThemeColor());
    }

    
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('PERMISO_GESTIONAR_PERSONAL')")
	public ResponseEntity<ApiResponse<Usuario>> obtenerUsuario(@PathVariable Long id) {
		try {
			Usuario usuariosOpt = service.obtenerUserPorId(id);
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(usuariosOpt)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
    

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('PERMISO_GESTIONAR_PERSONAL')")
	public ResponseEntity<ApiResponse<Usuario>>actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Usuario usuarioActualizado = service.updateUser(id, userDTO);
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(usuarioActualizado)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/cambiar-contraseña")
	public ResponseEntity<ApiResponse<Usuario>>actualizarContraseña(Authentication authentication, @Valid @RequestBody 
			ActualizarContrasenaDTO contraseñaDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Usuario usuarioActualizado = service.actualizarContraseña(authentication, contraseñaDTO);
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(usuarioActualizado)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

	@PatchMapping("/{id}/desactivar")
	@PreAuthorize("hasAuthority('PERMISO_GESTIONAR_PERSONAL')")
	public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
		try {
			service.deleteUser(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.NO_CONTENT.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.NO_CONTENT))
							.build(),
					HttpStatus.NO_CONTENT
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
	
	@PatchMapping("/{id}/activar")
	@PreAuthorize("hasAuthority('PERMISO_GESTIONAR_PERSONAL')")
	public ResponseEntity<ApiResponse<Void>> activarUsuario(@PathVariable Long id) {
		try {
			service.activeUser(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('PERMISO_GESTIONAR_PERSONAL')")
	public ResponseEntity<ApiResponse<Usuario>> guardarUsuario(@Valid @RequestBody UsuarioDTO grupoDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Usuario user = service.registrarUser(grupoDTO);
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(HttpStatus.CREATED.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.CREATED))
							.data(user)
							.build(),
					HttpStatus.CREATED
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
	
}
