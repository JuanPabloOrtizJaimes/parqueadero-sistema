package com.nelumbo.parqueadero.servicesimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nelumbo.parqueadero.entities.Usuario;
import com.nelumbo.parqueadero.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Intentando cargar usuario con email: {}", username);

		Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow(() -> {
			logger.error("No se encontró un usuario con el email: {}", username);
			return new UsernameNotFoundException("Usuario no encontrado con el email: " + username);
		});

		logger.info("Usuario encontrado: {}", usuario.getEmail());
		return usuario;
	}
}
