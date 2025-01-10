package com.nelumbo.parqueadero.servicesimpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.dtos.ParqueaderoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.entities.Parqueadero;
import com.nelumbo.parqueadero.entities.Rol;
import com.nelumbo.parqueadero.entities.Usuario;
import com.nelumbo.parqueadero.mappers.ParqueaderoMapper;
import com.nelumbo.parqueadero.mappers.VehiculoMapper;
import com.nelumbo.parqueadero.repositories.ParqueaderoRepository;
import com.nelumbo.parqueadero.repositories.UsuarioRepository;
import com.nelumbo.parqueadero.requests.ActualizarParqueaderoRequest;
import com.nelumbo.parqueadero.requests.EmailRequest;
import com.nelumbo.parqueadero.requests.ParqueaderoRequest;
import com.nelumbo.parqueadero.services.EmailService;
import com.nelumbo.parqueadero.services.ParqueaderoService;

@Service
public class ParqueaderoServiceImpl implements ParqueaderoService {

	private static final Logger logger = LoggerFactory.getLogger(ParqueaderoServiceImpl.class);

	@Autowired
	private ParqueaderoRepository parqueaderoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ParqueaderoMapper parqueaderoMapper;

	@Autowired
	private VehiculoMapper vehiculoMapper;

	@Autowired
	private EmailService emailService;

	@Override
	public ParqueaderoDTO crearParqueadero(ParqueaderoRequest parqueaderoRequest) {
		logger.info("Iniciando creación de parqueadero");
		try {
			Parqueadero parqueadero = parqueaderoMapper.paraEntidad(parqueaderoRequest);
			Parqueadero parqueaderoCreado = parqueaderoRepository.save(parqueadero);
			logger.info("Parqueadero creado exitosamente con ID: {}", parqueaderoCreado.getId());
			return parqueaderoMapper.paraDto(parqueaderoCreado);
		} catch (Exception e) {
			logger.error("Error al crear parqueadero", e);
			throw new RuntimeException("No se pudo crear el parqueadero", e);
		}
	}

	@Override
	public List<ParqueaderoDTO> obtenerParqueaderos() {
		logger.info("Obteniendo todos los parqueaderos");
		try {
			return parqueaderoRepository.findAll().stream().map(parqueaderoMapper::paraDto).toList();
		} catch (Exception e) {
			logger.error("Error al obtener parqueaderos", e);
			throw new RuntimeException("No se pudieron obtener los parqueaderos", e);
		}
	}

	@Override
	public Optional<ParqueaderoDTO> obtenerParqueaderoPorId(Long id) {
		logger.info("Buscando parqueadero con ID: {}", id);
		try {
			return parqueaderoRepository.findById(id).map(parqueaderoMapper::paraDto);
		} catch (Exception e) {
			logger.error("Error al obtener parqueadero con ID: {}", id, e);
			throw new RuntimeException("No se pudo obtener el parqueadero", e);
		}
	}

	@Override
	public List<VehiculoDTO> obtenerVehiculosPorIdParqueadero(Long id) {
		logger.info("Obteniendo vehículos del parqueadero con ID: {}", id);
		try {
			Parqueadero parqueadero = parqueaderoRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Parqueadero con ID " + id + " no encontrado"));
			return parqueadero.getVehiculos().stream().map(vehiculoMapper::paraDto).toList();
		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Error al obtener vehículos del parqueadero con ID: {}", id, e);
			throw new RuntimeException("No se pudieron obtener los vehículos", e);
		}
	}

	@Override
	public ParqueaderoDTO actualizarParqueadero(Long id, ActualizarParqueaderoRequest actualizarParqueaderoRequest) {
		logger.info("Actualizando parqueadero con ID: {}", id);
		try {
			Parqueadero parqueadero = parqueaderoRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Parqueadero con ID " + id + " no encontrado"));
			actualizarCamposParqueadero(parqueadero, actualizarParqueaderoRequest);
			Parqueadero parqueaderoActualizado = parqueaderoRepository.save(parqueadero);
			logger.info("Parqueadero actualizado exitosamente con ID: {}", id);
			return parqueaderoMapper.paraDto(parqueaderoActualizado);
		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Error al actualizar parqueadero con ID: {}", id, e);
			throw new RuntimeException("No se pudo actualizar el parqueadero", e);
		}
	}

	@Override
	public ParqueaderoDTO vincularSocioAParqueadero(Long id, String email) {
		logger.info("Vinculando socio con email: {} al parqueadero con ID: {}", email, id);
		try {
			Parqueadero parqueadero = parqueaderoRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Parqueadero con ID " + id + " no encontrado"));

			Usuario socio = usuarioRepository.findByEmail(email)
					.orElseThrow(() -> new IllegalArgumentException("Usuario con Email " + email + " no encontrado"));

			if (!socio.getRol().equals(Rol.SOCIO)) {
				throw new IllegalArgumentException("Usuario con Email " + email + " no tiene rol de socio");
			}

			parqueadero.setSocio(socio);
			Parqueadero parqueaderoActualizado = parqueaderoRepository.save(parqueadero);

			if (parqueaderoActualizado.getSocio() != null) {
				EmailRequest emailRequest = new EmailRequest(email, null,
						"Has sido vinculado como socio al parqueadero " + parqueadero.getNombre(),
						parqueadero.getNombre());
				MensajeResponse emailResponse = emailService.enviarCorreo(emailRequest);

				if (!"Correo Enviado".equals(emailResponse.getMensaje())) {
					logger.warn("No se pudo enviar el correo al socio con email: {}", email);
				} else {
					logger.info("Correo enviado exitosamente al socio con email: {}", email);
				}
			}

			return parqueaderoMapper.paraDto(parqueaderoActualizado);
		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Error al vincular socio con email: {} al parqueadero con ID: {}", email, id, e);
			throw new RuntimeException("No se pudo vincular el socio al parqueadero", e);
		}
	}

	@Override
	public void eliminarParqueadero(Long id) {
		logger.info("Eliminando parqueadero con ID: {}", id);
		try {
			Parqueadero parqueadero = parqueaderoRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Parqueadero con ID " + id + " no encontrado"));

			if (!parqueadero.getVehiculos().isEmpty()) {
				logger.warn("No se puede eliminar el parqueadero porque contiene vehículos");
				throw new IllegalStateException("No se puede eliminar el parqueadero porque contiene vehículos.");
			}

			parqueaderoRepository.delete(parqueadero);
			logger.info("Parqueadero con ID: {} eliminado exitosamente", id);
		} catch (IllegalArgumentException | IllegalStateException e) {
			logger.warn(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Error al eliminar parqueadero con ID: {}", id, e);
			throw new RuntimeException("No se pudo eliminar el parqueadero", e);
		}
	}

	private void actualizarCamposParqueadero(Parqueadero parqueadero, ActualizarParqueaderoRequest request) {
		if (request.getNombre() != null && !request.getNombre().isEmpty()) {
			parqueadero.setNombre(request.getNombre());
		}
		if (request.getCapacidad() > 0) {
			parqueadero.setCapacidad(request.getCapacidad());
		}
		if (request.getCostoPorHora() > 0) {
			parqueadero.setCostoPorHora(request.getCostoPorHora());
		}
		if (request.getEmail() != null) {
			Usuario socio = usuarioRepository.findByEmail(request.getEmail()).orElseThrow(
					() -> new IllegalArgumentException("Usuario con Email " + request.getEmail() + " no encontrado"));
			if (!socio.getRol().equals(Rol.SOCIO)) {
				throw new IllegalArgumentException(
						"Usuario con Email " + request.getEmail() + " no tiene rol de socio");
			}
			parqueadero.setSocio(socio);
		}
	}
}
