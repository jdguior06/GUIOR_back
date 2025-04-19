package com.sistema.pos.config;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sistema.pos.entity.Bitacora;
import com.sistema.pos.entity.Usuario;
import com.sistema.pos.repository.BitacoraRepository;
import com.sistema.pos.service.UsuarioDetailsService;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class BitacoraAspect {

	@Autowired
	private BitacoraRepository repository;
	
	@Autowired
	private UsuarioDetailsService usuarioDetailsService;
	
	@Autowired
	private HttpServletRequest request;
	
	private static final Logger bitacoraLogger = LoggerFactory.getLogger(BitacoraAspect.class);
	
	@Around("@annotation(LoggableAction)")
	public Object BitacoraAction(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String usuario = getAuthenticatedUsername();
		String ip = getClientIp();
		String dispositivo = obtenerInformacionDispositivo();
		String actionName = joinPoint.getSignature().getName();
		Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            throw e; 
        }
		Bitacora bitacora = new Bitacora();
		bitacora.setEvento(actionName);
		bitacora.setFecha(LocalDateTime.now(ZoneId.of("America/La_Paz")));
		bitacora.setIp(ip);
		bitacora.setUsuario(usuario);
		bitacora.setDispositivo(dispositivo);
		repository.save(bitacora);
		
		bitacoraLogger.info("Evento registrado: Usuario: {}, IP: {}, Acción: {}, Dispositivo: {}, Fecha: {}",
                bitacora.getUsuario(),
                bitacora.getIp(),
                bitacora.getEvento(),
                bitacora.getDispositivo(),
                bitacora.getFecha());
		
		return result;
	}
	
	private String obtenerInformacionDispositivo() {
		String userAgent = request.getHeader("User-Agent");
	    return userAgent != null && userAgent.length() > 128 
	           ? userAgent.substring(0, 128) 
	           : userAgent;
    }

	private String getClientIp() {
	    String ipAddress = request.getHeader("X-Forwarded-For");
	    if (ipAddress == null || ipAddress.isEmpty()) {
	        ipAddress = request.getRemoteAddr();
	    }
	    return ipAddress;
	}
	
	private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == "anonymousUser") {
            return "Usuario anónimo";
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario user = usuarioDetailsService.getUser(userDetails.getUsername());
        return user.getEmail();
    }
	
}
