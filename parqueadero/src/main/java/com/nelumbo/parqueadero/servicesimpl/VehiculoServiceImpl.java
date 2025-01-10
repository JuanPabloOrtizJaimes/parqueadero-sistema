package com.nelumbo.parqueadero.servicesimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.dtos.ParqueaderoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.entities.Historial;
import com.nelumbo.parqueadero.entities.Parqueadero;
import com.nelumbo.parqueadero.entities.Vehiculo;
import com.nelumbo.parqueadero.mappers.ParqueaderoMapper;
import com.nelumbo.parqueadero.mappers.VehiculoMapper;
import com.nelumbo.parqueadero.repositories.HistorialRepository;
import com.nelumbo.parqueadero.repositories.ParqueaderoRepository;
import com.nelumbo.parqueadero.repositories.VehiculoRepository;
import com.nelumbo.parqueadero.requests.EmailRequest;
import com.nelumbo.parqueadero.requests.VehiculoRequest;
import com.nelumbo.parqueadero.services.EmailService;
import com.nelumbo.parqueadero.services.VehiculoService;

@Service
public class VehiculoServiceImpl implements VehiculoService {

	private static final Logger logger = LoggerFactory.getLogger(VehiculoServiceImpl.class);

	@Autowired
	private ParqueaderoRepository parqueaderoRepository;

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@Autowired
	private HistorialRepository historialRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ParqueaderoMapper parqueaderoMapper;

	@Autowired
	private VehiculoMapper vehiculoMapper;

	@Override
	public MensajeResponse registrarVehiculo(VehiculoRequest vehiculoRequest) {
		String emailLogueado = obtenerEmailLogueado();
		String placa = vehiculoRequest.getPlaca().toUpperCase();

		logger.info("Intentando registrar vehículo con placa: {}", placa);

		if (!esPlacaValida(placa)) {
			logger.warn("Placa inválida: {}", placa);
			return new MensajeResponse("Placa inválida. Debe tener 6 caracteres alfanuméricos sin la letra ñ.");
		}

		if (existePlacaEnParqueaderos(placa)) {
			logger.warn("La placa {} ya existe en algún parqueadero", placa);
			return new MensajeResponse("No se puede registrar ingreso, ya existe la placa en este u otro parqueadero.");
		}

		Optional<Parqueadero> parqueaderoOptional = parqueaderoRepository.findById(vehiculoRequest.getIdParqueadero());
		if (parqueaderoOptional.isEmpty()) {
			logger.error("El parqueadero con ID {} no existe", vehiculoRequest.getIdParqueadero());
			return new MensajeResponse("El parqueadero especificado no existe.");
		}

		Parqueadero parqueadero = parqueaderoOptional.get();

		if (!esSocioDelParqueadero(parqueadero, emailLogueado)) {
			logger.warn("Usuario {} no es socio del parqueadero {}", emailLogueado, parqueadero.getNombre());
			return new MensajeResponse("Tú no eres el socio de este parqueadero.");
		}

		if (parqueadero.getVehiculos().size() >= parqueadero.getCapacidad()) {
			logger.warn("El parqueadero {} está lleno", parqueadero.getNombre());
			return new MensajeResponse("El parqueadero está lleno. No se pueden registrar más vehículos.");
		}

		Vehiculo vehiculo = registrarVehiculoEnParqueadero(placa, parqueadero);
		logger.info("Vehículo con placa {} registrado exitosamente en el parqueadero {}", placa,
				parqueadero.getNombre());

		enviarCorreoIngreso(parqueadero, vehiculo);

		return new MensajeResponse(vehiculo.getId());
	}

	private String obtenerEmailLogueado() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private boolean esPlacaValida(String placa) {
		return placa != null && placa.matches("^[a-zA-Z0-9]{6}$");
	}

	private boolean existePlacaEnParqueaderos(String placa) {
		return parqueaderoRepository.findAll().stream().flatMap(parqueadero -> parqueadero.getVehiculos().stream())
				.anyMatch(vehiculo -> vehiculo.getPlaca().equalsIgnoreCase(placa));
	}

	private boolean esSocioDelParqueadero(Parqueadero parqueadero, String emailLogueado) {
		return parqueadero.getSocio() != null && parqueadero.getSocio().getEmail().equals(emailLogueado);
	}

	private Vehiculo registrarVehiculoEnParqueadero(String placa, Parqueadero parqueadero) {
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPlaca(placa);
		vehiculo.setParqueadero(parqueadero);
		vehiculo.setFechaIngreso(LocalDateTime.now());
		return vehiculoRepository.save(vehiculo);
	}

	private void enviarCorreoIngreso(Parqueadero parqueadero, Vehiculo vehiculo) {
		String emailSocio = parqueadero.getSocio().getEmail();
		EmailRequest emailRequest = new EmailRequest(emailSocio, vehiculo.getPlaca(), "El vehículo con placas "
				+ vehiculo.getPlaca() + " ha ingresado al parqueadero " + parqueadero.getNombre(),
				parqueadero.getNombre());

		MensajeResponse emailResponse = emailService.enviarCorreo(emailRequest);
		if (!"Correo Enviado".equals(emailResponse.getMensaje())) {
			logger.error("Error al enviar el correo al socio: {}", emailSocio);
		} else {
			logger.info("Correo enviado exitosamente al socio: {}", emailSocio);
		}
	}

	@Override
	public MensajeResponse registrarSalidaVehiculo(VehiculoRequest vehiculoRequest) {
		String placa = vehiculoRequest.getPlaca().toUpperCase();

		logger.info("Intentando registrar salida de vehículo con placa: {}", placa);

		if (!esPlacaValida(placa)) {
			logger.warn("Placa inválida: {}", placa);
			return new MensajeResponse("Placa inválida. Debe tener 6 caracteres alfanuméricos sin la letra ñ.");
		}

		Optional<Parqueadero> parqueaderoOptional = parqueaderoRepository.findById(vehiculoRequest.getIdParqueadero());
		if (parqueaderoOptional.isEmpty()) {
			logger.error("El parqueadero con ID {} no existe", vehiculoRequest.getIdParqueadero());
			return new MensajeResponse("El parqueadero especificado no existe.");
		}

		Parqueadero parqueadero = parqueaderoOptional.get();
		if (!esSocioDelParqueadero(parqueadero, obtenerEmailLogueado())) {
			logger.warn("Usuario {} no es socio del parqueadero {}", obtenerEmailLogueado(), parqueadero.getNombre());
			return new MensajeResponse("Tú no eres el socio de este parqueadero.");
		}

		Optional<Vehiculo> vehiculoOptional = vehiculoRepository.findByPlaca(placa);
		if (vehiculoOptional.isPresent()) {
			Vehiculo vehiculo = vehiculoOptional.get();
			if (vehiculo.getParqueadero() != null
					&& vehiculo.getParqueadero().getId().equals(vehiculoRequest.getIdParqueadero())) {
				registrarHistorialYEliminarVehiculo(vehiculoOptional.get());
				logger.info("Salida del vehículo con placa {} registrada exitosamente", placa);
				return new MensajeResponse("Salida registrada");
			} else {
				logger.warn("El vehículo con placa {} está en otro parqueadero", placa);
				return new MensajeResponse("El vehículo está pero en otro parqueadero.");
			}
		}

		logger.warn("No se puede registrar salida. La placa {} no existe en el parqueadero.", placa);
		return new MensajeResponse("No se puede registrar salida, no existe la placa en el parqueadero.");
	}

	private void registrarHistorialYEliminarVehiculo(Vehiculo vehiculo) {
		Historial historial = new Historial();
		historial.setPlaca(vehiculo.getPlaca());
		historial.setFechaIngreso(vehiculo.getFechaIngreso());
		historial.setFechaSalida(LocalDateTime.now());
		historial.setParqueadero(vehiculo.getParqueadero());
		BigDecimal monto = calcularMonto(historial.getFechaIngreso(), historial.getFechaSalida(),
				vehiculo.getParqueadero().getCostoPorHora());
		historial.setMonto(monto);

		vehiculoRepository.delete(vehiculo);
		historialRepository.save(historial);

		logger.info("Historial registrado para el vehículo con placa: {}", vehiculo.getPlaca());
	}

	public BigDecimal calcularMonto(LocalDateTime fechaIngreso, LocalDateTime fechaSalida, Double tarifaPorHora) {
		long minutosEstacionado = Duration.between(fechaIngreso, fechaSalida).toMinutes();

		long bloquesDe15Minutos = (minutosEstacionado + 14) / 15;

		BigDecimal tarifaPorBloqueDe15 = BigDecimal.valueOf(tarifaPorHora / 4);

		BigDecimal monto = tarifaPorBloqueDe15.multiply(BigDecimal.valueOf(bloquesDe15Minutos));

		monto = monto.setScale(2, RoundingMode.HALF_UP);

		logger.info("Monto calculado: {} para {} minutos estacionado", monto, minutosEstacionado);
		return monto;
	}

	@Override
	public List<ParqueaderoDTO> obtenerParqueaderosAsociados() {
		logger.info("Obteniendo parqueaderos asociados para el usuario logueado");
		return parqueaderosDeEmailLogueado().map(parqueaderoMapper::paraDto).toList();
	}

	@Override
	public List<VehiculoDTO> obtenerVehiculosPorIdParqueaderoAsociado(Long id) {
		logger.info("Obteniendo vehículos para el parqueadero con ID {} asociado al usuario logueado", id);
		return parqueaderosDeEmailLogueado().filter(parqueadero -> parqueadero.getId().equals(id))
				.flatMap(parqueadero -> parqueadero.getVehiculos().stream()).map(vehiculoMapper::paraDto).toList();
	}

	public Stream<Parqueadero> parqueaderosDeEmailLogueado() {
		String emailLogueado = obtenerEmailLogueado();
		return parqueaderoRepository.findAll().stream().filter(parqueadero -> parqueadero.getSocio() != null
				&& parqueadero.getSocio().getEmail().equals(emailLogueado));
	}
}
