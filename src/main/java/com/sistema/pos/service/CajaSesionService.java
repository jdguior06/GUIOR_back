package com.sistema.pos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.pos.config.LoggableAction;
import com.sistema.pos.dto.CajaSesionDTO;
import com.sistema.pos.entity.Caja;
import com.sistema.pos.entity.CajaSesion;
import com.sistema.pos.entity.Usuario;
import com.sistema.pos.entity.Venta;
import com.sistema.pos.repository.CajaSesionRepository;

@Service
public class CajaSesionService {
	
	@Autowired
	private CajaSesionRepository cajaSesionRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private CajaService cajaService;
	
	public List<CajaSesion> listarCajasSesiones() {
		return cajaSesionRepository.findAll();
	}
	
	@LoggableAction
	public CajaSesion aperturaDeCaja(CajaSesionDTO cajaSesionDTO) {
		Caja caja = cajaService.findById(cajaSesionDTO.getId_caja());
        Optional<CajaSesion> sesionAbierta = cajaSesionRepository.findByCajaIdAndAbiertaTrue(caja.getId());

        if (sesionAbierta.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La caja ya tiene una sesión abierta.");
        }
		
		Usuario usuario = usuarioService.obtenerUsuarioAuthenticado();
		CajaSesion cajaSesion = new CajaSesion();
		cajaSesion.setUsuario(usuario);
		cajaSesion.setCaja(caja);
		cajaSesion.setSaldoInicial(cajaSesionDTO.getMonto());
		cajaSesion.setFechaHoraApertura(LocalDateTime.now());
		cajaSesion.setAbierta(true);
		return cajaSesionRepository.save(cajaSesion);
	}
	
	public CajaSesion obtenerCajaSesion(Long id) {
		CajaSesion cajaSesion = cajaSesionRepository.findById(id)
		        .orElseThrow(() -> new IllegalArgumentException("No se encontró la sesión de caja especificada."));
		    
		    if (cajaSesion.getAbierta()) {
		        throw new IllegalStateException("La caja ya está cerrada.");
		    }
		return cajaSesion;
	}
	
    public Optional<CajaSesion> obtenerSesionAbiertaPorCaja(Long idCaja) {
        return cajaSesionRepository.findByCajaIdAndAbiertaTrue(idCaja);
    }
    
    public Optional<CajaSesion> obtenerSesionAbiertaPorCajaYUsuario(Long idCaja) {
    	Usuario usuario = usuarioService.obtenerUsuarioAuthenticado();
        return cajaSesionRepository.findByCajaIdAndUsuarioIdAndAbiertaTrue(idCaja, usuario.getId());
    }
	
	public CajaSesion obtenerSesionAbierta(Long idCaja) {
        return cajaSesionRepository.findByCajaIdAndAbiertaTrue(idCaja)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay ninguna sesión abierta para la caja especificada."));
    }

	@LoggableAction
	public CajaSesion cierreDeCaja(Long cajaSesionId) {
	    CajaSesion cajaSesion = obtenerCajaSesion(cajaSesionId);
	    
	    Double totalVentas = cajaSesion.getVentas().stream()
	        .mapToDouble(Venta::getTotal)
	        .sum();
	    
	    cajaSesion.setSaldoFinal(cajaSesion.getSaldoInicial() + totalVentas);
	    cajaSesion.setFechaHoraCierre(LocalDateTime.now());
	    cajaSesion.setTotalVentas(totalVentas);
	    cajaSesion.setAbierta(false);

	    return cajaSesionRepository.save(cajaSesion);
	}
	
}
