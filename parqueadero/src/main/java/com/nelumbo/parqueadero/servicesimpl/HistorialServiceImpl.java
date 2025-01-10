package com.nelumbo.parqueadero.servicesimpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nelumbo.parqueadero.dtos.HistorialDTO;
import com.nelumbo.parqueadero.dtos.ParqueaderoGananciaDTO;
import com.nelumbo.parqueadero.dtos.SocioIngresoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.exceptions.DatosNoEncontradosException;
import com.nelumbo.parqueadero.mappers.VehiculoMapper;
import com.nelumbo.parqueadero.repositories.HistorialCountProjection;
import com.nelumbo.parqueadero.repositories.HistorialParqueaderoGananciaProjection;
import com.nelumbo.parqueadero.repositories.HistorialRepository;
import com.nelumbo.parqueadero.repositories.SocioIngresoProjection;
import com.nelumbo.parqueadero.repositories.VehiculoRepository;
import com.nelumbo.parqueadero.services.HistorialService;

@Service
public class HistorialServiceImpl implements HistorialService {

	private static final Logger logger = LoggerFactory.getLogger(HistorialServiceImpl.class);

	@Autowired
	private HistorialRepository historialRepository;

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@Autowired
	private VehiculoMapper vehiculoMapper;

	@Override
	public List<HistorialDTO> vehiculosConMasRegistrosEnParqueaderos() {
		logger.info("Obteniendo vehículos con más registros en todos los parqueaderos.");
		Pageable pageable = PageRequest.of(0, 10);

		List<HistorialCountProjection> historialVehiculos = historialRepository
				.vehiculosConMasRegistrosEnParqueaderos(pageable);

		return historialVehiculos.stream().map(h -> new HistorialDTO(h.getPlaca(), h.getCount()))
				.collect(Collectors.toList());
	}

	@Override
	public List<HistorialDTO> vehiculosConMasRegistrosEnUnParqueadero(Long parqueaderoId) {
		logger.info("Obteniendo vehículos con más registros en el parqueadero con ID: {}", parqueaderoId);
		Pageable pageable = PageRequest.of(0, 10);

		List<HistorialCountProjection> historialVehiculos = historialRepository
				.vehiculosConMasRegistrosEnUnParqueadero(parqueaderoId, pageable);

		return historialVehiculos.stream().map(h -> new HistorialDTO(h.getPlaca(), h.getCount()))
				.collect(Collectors.toList());
	}

	@Override
	public List<VehiculoDTO> vehiculosPrimerIngreso(Long parqueaderoId) {
		logger.info("Obteniendo vehículos con primer ingreso en el parqueadero con ID: {}", parqueaderoId);
		return vehiculoRepository.vehiculosPrimerIngreso(parqueaderoId).stream().map(vehiculoMapper::paraDto).toList();
	}

	@Override
	public List<ParqueaderoGananciaDTO> obtenerTop3ParqueaderosGananciaSemana() {
		logger.info("Obteniendo los 3 parqueaderos con más ganancias de la semana actual.");
		LocalDateTime inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
		LocalDateTime finSemana = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

		Pageable pageable = PageRequest.of(0, 3);

		List<HistorialParqueaderoGananciaProjection> parqueaderosGanancias = historialRepository
				.obtenerTop3ParqueaderosGananciaSemana(inicioSemana, finSemana, pageable);

		return parqueaderosGanancias.stream().map(p -> new ParqueaderoGananciaDTO(p.getParqueadero().getId(),
				p.getParqueadero().getNombre(), p.getTotalGanancia())).collect(Collectors.toList());
	}

	@Override
	public ParqueaderoGananciaDTO obtenerGananciasHoy(Long parqueaderoId) {
		logger.info("Obteniendo ganancias del día para el parqueadero con ID: {}", parqueaderoId);
		LocalDateTime fechaHoy = LocalDate.now().atStartOfDay();

		HistorialParqueaderoGananciaProjection p = historialRepository.obtenerGananciasHoy(parqueaderoId, fechaHoy);
		if (p == null) {
			logger.warn("No se encontraron ganancias para el parqueadero con ID: {}", parqueaderoId);
			throw new DatosNoEncontradosException(
					"No se encontraron datos para el parqueadero con ID: " + parqueaderoId);
		}

		return new ParqueaderoGananciaDTO(p.getParqueadero().getId(), p.getParqueadero().getNombre(),
				p.getTotalGanancia());
	}

	@Override
	public ParqueaderoGananciaDTO obtenerGananciasSemana(Long parqueaderoId) {
		logger.info("Obteniendo ganancias semanales para el parqueadero con ID: {}", parqueaderoId);
		LocalDateTime inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
		LocalDateTime finSemana = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

		HistorialParqueaderoGananciaProjection p = historialRepository.obtenerGananciasSemana(parqueaderoId,
				inicioSemana, finSemana);
		if (p == null) {
			logger.warn("No se encontraron ganancias semanales para el parqueadero con ID: {}", parqueaderoId);
			throw new DatosNoEncontradosException(
					"No se encontraron datos para el parqueadero con ID: " + parqueaderoId);
		}

		return new ParqueaderoGananciaDTO(p.getParqueadero().getId(), p.getParqueadero().getNombre(),
				p.getTotalGanancia());
	}

	@Override
	public ParqueaderoGananciaDTO obtenerGananciasMes(Long parqueaderoId) {
		logger.info("Obteniendo ganancias mensuales para el parqueadero con ID: {}", parqueaderoId);
		LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		LocalDateTime finMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

		HistorialParqueaderoGananciaProjection p = historialRepository.obtenerGananciasMes(parqueaderoId, inicioMes,
				finMes);
		if (p == null) {
			logger.warn("No se encontraron ganancias mensuales para el parqueadero con ID: {}", parqueaderoId);
			throw new DatosNoEncontradosException(
					"No se encontraron datos para el parqueadero con ID: " + parqueaderoId);
		}

		return new ParqueaderoGananciaDTO(p.getParqueadero().getId(), p.getParqueadero().getNombre(),
				p.getTotalGanancia());
	}

	@Override
	public ParqueaderoGananciaDTO obtenerGananciasAnio(Long parqueaderoId) {
		logger.info("Obteniendo ganancias anuales para el parqueadero con ID: {}", parqueaderoId);
		LocalDateTime inicioAnio = LocalDate.now().withDayOfYear(1).atStartOfDay();
		LocalDateTime finAnio = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()).atTime(23, 59, 59);

		HistorialParqueaderoGananciaProjection p = historialRepository.obtenerGananciasAnio(parqueaderoId, inicioAnio,
				finAnio);
		if (p == null) {
			logger.warn("No se encontraron ganancias anuales para el parqueadero con ID: {}", parqueaderoId);
			throw new DatosNoEncontradosException(
					"No se encontraron datos para el parqueadero con ID: " + parqueaderoId);
		}

		return new ParqueaderoGananciaDTO(p.getParqueadero().getId(), p.getParqueadero().getNombre(),
				p.getTotalGanancia());
	}

	@Override
	public List<SocioIngresoDTO> obtenerTop3SociosSemanaActual() {
		logger.info("Obteniendo los 3 socios con más ingresos de la semana actual.");
		LocalDateTime inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
		LocalDateTime finSemana = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

		Pageable top3 = PageRequest.of(0, 3);

		List<SocioIngresoProjection> socioIngresoProjections = vehiculoRepository
				.top3SociosConMasVehiculos(inicioSemana, finSemana, top3);
		return socioIngresoProjections.stream()
				.map(s -> new SocioIngresoDTO(s.getSocioId(), s.getSocioNombre(), s.getCantidadVehiculos()))
				.collect(Collectors.toList());
	}
}
