package com.sistema.pos.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.pos.config.JwtService;
import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.ActualizarContraseñaDTO;
import com.sistema.pos.dto.UsuarioDTO;
import com.sistema.pos.entity.Rol;
import com.sistema.pos.entity.Usuario;
import com.sistema.pos.repository.RolRepository;
import com.sistema.pos.repository.UsuarioRepository;
import com.sistema.pos.response.AuthResponse;
import com.sistema.pos.response.LoginRequest;
import com.sistema.pos.util.EmailService;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@Autowired
	private RolService rolService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UsuarioDetailsService usuarioDetailsService;

	public List<UsuarioDTO> listUser() {
		List<Usuario> user = usuarioRepository.findAll();
        return user.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
	}
	
	public List<Usuario> listUsuario() {
		return usuarioRepository.findAll();
	}

	public Long getUsuariorById(String name) {
		Usuario usuario = usuarioRepository.findByEmail(name)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	        return usuario.getId();
	}
	
	public Usuario obtenerUserPorId(Long id) {
		Optional<Usuario> user = usuarioRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}else {
			throw new UsernameNotFoundException("El usuario no se encuentra");
		}
	}
	
	@LoggableAction
	public AuthResponse createUser(UsuarioDTO userDto) {
		Optional<Rol> optionalUserRole = rolRepository.findByNombre("CAJERO");
		Rol userRole = optionalUserRole.orElseGet(() -> rolRepository.save(new Rol ("CAJERO")));
		Set<Rol> roles = Collections.singleton(userRole);
		Usuario usuario = new Usuario(userDto.getNombre(), 
				userDto.getApellido(), userDto.getEmail(), 
				passwordEncoder.encode(userDto.getPassword()), roles);
		usuarioRepository.save(usuario);
		return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
	}
	
	@LoggableAction
	public Usuario registrarUser(UsuarioDTO userDto) {
		try {
			Rol roles = rolService.obtenerRol(userDto.getRolId());
			Set<Rol> rolesSet = new HashSet<>();
		    rolesSet.add(roles);
		    
		    String password = userDto.getPassword();		    
		    
		    if (password == null || password.trim().isEmpty()) {
		        throw new RuntimeException("Error: La contraseña generada es inválida.");
		    }
		    
		    String passwordEncriptada = passwordEncoder.encode(password);
		    
			Usuario usuario = new Usuario(userDto.getNombre(), 
					userDto.getApellido(), userDto.getEmail(), 
					passwordEncriptada, rolesSet);
			usuario = usuarioRepository.save(usuario);

			try {
				emailService.enviarCorreoBienvenida(usuario.getEmail(), usuario.getNombre(), password);
			} catch (MessagingException e) {
				throw new RuntimeException("Error al enviar el correo.");
			}
			return usuario;
			
		} catch (Exception e) {
			 throw new RuntimeException("Error al registrar el usuario.");
		}
		
	}
	
	public Usuario actualizarContraseña(Authentication authentication, ActualizarContraseñaDTO contraseñaDTO) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Usuario user = usuarioDetailsService.getUser(userDetails.getUsername());
		
		if (!passwordEncoder.matches(contraseñaDTO.getContraseñaActual(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es incorrecta.");
		}
		
		if (!contraseñaDTO.getNuevaContraseña().equals(contraseñaDTO.getConfirmarContraseña())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las nuevas contraseñas no coinciden.");
		}

		if (passwordEncoder.matches(contraseñaDTO.getNuevaContraseña(), user.getPassword())) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña no puede ser igual a la anterior.");
	    }
		
		String passwordEncriptada = passwordEncoder.encode(contraseñaDTO.getNuevaContraseña());
		user.setPassword(passwordEncriptada);
		
		user =  usuarioRepository.save(user);
		
		try {
			emailService.enviarCorreoCambioContrasena(user.getEmail(), user.getNombre());
		} catch (MessagingException e) {
			throw new RuntimeException("Error al enviar el correo.");
		}
		
		return user;
	}
	
	public AuthResponse createUserAdmin(UsuarioDTO userDto) {
		Optional<Rol> optionalUserRole = rolRepository.findByNombre("ADMIN");
		Rol userRole = optionalUserRole.orElseGet(() -> rolRepository.save(new Rol ("ADMIN")));
		Set<Rol> roles = Collections.singleton(userRole);
		Usuario usuario = new Usuario(userDto.getNombre(), 
				userDto.getApellido(), userDto.getEmail(), 
				passwordEncoder.encode(userDto.getPassword()), roles);
		usuarioRepository.save(usuario);
		return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
	}

	public Usuario getUser(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		return usuario;
	}


	public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails user = usuarioRepository.findByEmail(loginRequest.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
		Usuario userDetails = this.getUser(user.getUsername());
        return AuthResponse.builder()
            .token(token)
			.email(userDetails.getEmail())
				.role(userDetails.getRol().iterator().next())
				.nombre(userDetails.getNombre())
				.apellido(userDetails.getApellido())
				.id(userDetails.getId())
				.themeColor(userDetails.getThemeColor())
            .build();
	}
	
	public AuthResponse loader(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.getToken(userDetails);
		Usuario user = usuarioDetailsService.getUser(userDetails.getUsername());
        return AuthResponse.builder()
        	.token(token)
			.email(user.getEmail())
			.role(user.getRol().iterator().next())
			.nombre(user.getNombre())
			.apellido(user.getApellido())
			.id(user.getId())
			.themeColor(user.getThemeColor())
            .build();
	}
	
	@LoggableAction
	public Usuario updateThemeColor(Authentication authentication, String themeColor) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Usuario user = usuarioDetailsService.getUser(userDetails.getUsername());
        user.setThemeColor(themeColor);
        return usuarioRepository.save(user);
    }
	
	@LoggableAction
	public Usuario updateUser(Long id, UsuarioDTO userDto) {
		Usuario user = obtenerUserPorId(id);
		user.setNombre(userDto.getNombre());
		user.setApellido(userDto.getApellido());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return usuarioRepository.save(user);
	}
	
	@Transactional
	@LoggableAction
	public void deleteUser(Long id) {
		Usuario user = obtenerUserPorId(id);
		user.setActivo(false);
		user.setCredencialesNoExpiradas(false);
		user.setCuentaNoBloqueada(false);
		user.setCuentaNoBloqueada(false);
		usuarioRepository.save(user);
	}
	
	@Transactional
	@LoggableAction
	public void activeUser(Long id) {
		Usuario user = obtenerUserPorId(id);
		user.setActivo(true);
		user.setCredencialesNoExpiradas(true);
		user.setCuentaNoBloqueada(true);
		user.setCuentaNoBloqueada(true);
		usuarioRepository.save(user);
	}
	
	public List<Usuario> buscarUsuarios(String searchTerm) {
        return usuarioRepository.findByNombreOrApellidoOrEmail(searchTerm);
    }
	
	public Usuario obtenerUsuarioAuthenticado(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String email = ((UserDetails) principal).getUsername();
			return getUser(email);
		} else {
			throw new IllegalStateException("Usuario no autenticado");
		}
	}
	
}
