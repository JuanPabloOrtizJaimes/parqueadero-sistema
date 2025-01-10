package com.nelumbo.parqueadero.servicesimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelumbo.parqueadero.components.JwtTokenProvider;
import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.entities.Rol;
import com.nelumbo.parqueadero.entities.Usuario;
import com.nelumbo.parqueadero.exceptions.AutenticacionException;
import com.nelumbo.parqueadero.exceptions.UsuarioYaExisteException;
import com.nelumbo.parqueadero.repositories.UsuarioRepository;
import com.nelumbo.parqueadero.requests.RegistroRequest;
import com.nelumbo.parqueadero.services.AutenticacionService;

@Service
public class AutenticacionServiceImpl implements AutenticacionService {

	private static final Logger logger = LoggerFactory.getLogger(AutenticacionServiceImpl.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public MensajeResponse authenticateAndGetToken(String email, String password) {
		try {
			logger.info("Iniciando autenticación para el usuario: {}", email);
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			String token = jwtTokenProvider.generateToken(authentication);
			logger.info("Autenticación exitosa para el usuario: {}", email);
			return new MensajeResponse(token);
		} catch (Exception ex) {
			logger.error("Error durante la autenticación para el usuario: {}", email, ex);
			throw new AutenticacionException("Credenciales inválidas o usuario no encontrado.");
		}
	}

	@Override
	public MensajeResponse registrarUsuarioSocio(RegistroRequest registroRequest) {
		try {
			logger.info("Registrando nuevo usuario con email: {}", registroRequest.getEmail());

			if (usuarioRepository.findByEmail(registroRequest.getEmail()).isPresent()) {
				logger.warn("Intento de registro con un email ya existente: {}", registroRequest.getEmail());
				throw new UsuarioYaExisteException("El email ya existe");
			}

			Usuario usuario = new Usuario();
			usuario.setNombre(registroRequest.getNombre());
			usuario.setEmail(registroRequest.getEmail());
			usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
			usuario.setRol(Rol.SOCIO);

			Usuario usuarioGuardado = usuarioRepository.save(usuario);
			logger.info("Usuario registrado exitosamente con ID: {}", usuarioGuardado.getId());
			return new MensajeResponse(usuarioGuardado.getId());
		} catch (UsuarioYaExisteException ex) {
			throw ex; 
		} catch (Exception ex) {
			logger.error("Error durante el registro del usuario: {}", registroRequest.getEmail(), ex);
			throw new RuntimeException("Error inesperado durante el registro del usuario");
		}
	}
}
