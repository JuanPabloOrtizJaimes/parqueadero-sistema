package com.nelumbo.parqueadero.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.nelumbo.parqueadero.entities.Rol;
import com.nelumbo.parqueadero.entities.Usuario;
import com.nelumbo.parqueadero.repositories.UsuarioRepository;

@Component
public class PrecargaAdmin implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(PrecargaAdmin.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void run(String... args) throws Exception {
		String adminEmail = "admin@mail.com";

		if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
			Usuario admin = new Usuario();
			admin.setNombre("admin");
			admin.setEmail(adminEmail);
			admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
			admin.setRol(Rol.ADMIN);

			usuarioRepository.save(admin);
			logger.info("Usuario ADMIN creado: " + adminEmail);
		}
	}

}
