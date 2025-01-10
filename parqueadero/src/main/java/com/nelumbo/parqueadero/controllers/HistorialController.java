package com.nelumbo.parqueadero.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.parqueadero.dtos.HistorialDTO;
import com.nelumbo.parqueadero.dtos.ParqueaderoGananciaDTO;
import com.nelumbo.parqueadero.dtos.SocioIngresoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.services.HistorialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/historial")
@Tag(name = "Historial", description = "Seccion para realizar acciones relacionadas con los indicadores")
public class HistorialController {

	@Autowired
	private HistorialService historialService;

	@Operation(summary = "Obtener vehiculos con mas registros en parqueaderos", description = "Endpoint que permite la obtencion de los vehiculos que mas se han registrado en parqueaderos y cuantas veces")
	@GetMapping("/top-vehiculos")
	public ResponseEntity<List<HistorialDTO>> getVehiculosConMasRegistrosEnParqueaderos() {
		List<HistorialDTO> topVehicles = historialService.vehiculosConMasRegistrosEnParqueaderos();
		return ResponseEntity.ok(topVehicles);
	}

	@Operation(summary = "Obtener vehiculos con mas registros en un parqueadero", description = "Endpoint que permite la obtencion de los vehiculos que mas se han registrado en un parqueadero y cuantas veces")
	@GetMapping("/top-vehiculos/{parqueaderoId}")
	public ResponseEntity<List<HistorialDTO>> getTopVehiculosConMasRegistrosEnUnParqueadero(
			@PathVariable Long parqueaderoId) {
		List<HistorialDTO> topVehicles = historialService.vehiculosConMasRegistrosEnUnParqueadero(parqueaderoId);
		return ResponseEntity.ok(topVehicles);
	}

	@Operation(summary = "Obtener vehiculos de primer ingreso en un parqueadero", description = "Endpoint que permite la obtencion de los vehiculos que se registran por primera vez en un parqueadero")
	@GetMapping("/vehiculos-primer-ingreso/{parqueaderoId}")
	public ResponseEntity<List<VehiculoDTO>> getVehiculosPrimerIngreso(@PathVariable Long parqueaderoId) {
		List<VehiculoDTO> vehiculoDTOs = historialService.vehiculosPrimerIngreso(parqueaderoId);
		return ResponseEntity.ok(vehiculoDTOs);
	}

	@Operation(summary = "Obtener ganancias del dia de un parqueadero", description = "Endpoint que permite la obtencion de las ganancias de un parqueadero el dia de hoy")
	@PreAuthorize("hasRole('SOCIO')")
	@GetMapping("/parqueadero-ganancias-hoy/{parqueaderoId}")
	public ResponseEntity<ParqueaderoGananciaDTO> obtenerGananciasHoy(@PathVariable Long parqueaderoId) {
		ParqueaderoGananciaDTO gananciaDTO = historialService.obtenerGananciasHoy(parqueaderoId);
		return ResponseEntity.ok(gananciaDTO);
	}

	@Operation(summary = "Obtener ganancias de la semana de un parqueadero", description = "Endpoint que permite la obtencion de las ganancias de un parqueadero de la semana vigente")
	@PreAuthorize("hasRole('SOCIO')")
	@GetMapping("/parqueadero-ganancias-semana/{parqueaderoId}")
	public ResponseEntity<ParqueaderoGananciaDTO> obtenerGananciasSemana(@PathVariable Long parqueaderoId) {
		ParqueaderoGananciaDTO gananciaDTO = historialService.obtenerGananciasSemana(parqueaderoId);
		return ResponseEntity.ok(gananciaDTO);
	}

	@Operation(summary = "Obtener ganancias del mes de un parqueadero", description = "Endpoint que permite la obtencion de las ganancias de un parqueadero del mes vigente")
	@PreAuthorize("hasRole('SOCIO')")
	@GetMapping("/parqueadero-ganancias-mes/{parqueaderoId}")
	public ResponseEntity<ParqueaderoGananciaDTO> obtenerGananciasMes(@PathVariable Long parqueaderoId) {
		ParqueaderoGananciaDTO gananciaDTO = historialService.obtenerGananciasMes(parqueaderoId);
		return ResponseEntity.ok(gananciaDTO);
	}

	@Operation(summary = "Obtener ganancias del anio de un parqueadero", description = "Endpoint que permite la obtencion de las ganancias de un parqueadero del anio vigente")
	@PreAuthorize("hasRole('SOCIO')")
	@GetMapping("/parqueadero-ganancias-anio/{parqueaderoId}")
	public ResponseEntity<ParqueaderoGananciaDTO> obtenerGananciasAnio(@PathVariable Long parqueaderoId) {
		ParqueaderoGananciaDTO gananciaDTO = historialService.obtenerGananciasAnio(parqueaderoId);
		return ResponseEntity.ok(gananciaDTO);
	}

	@Operation(summary = "Obtener top 3 de socios de la semana", description = "Endpoint que permite la obtencion del top 3 de socios con mas ganancias hechas en la semana vigente")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/top3-socios-semana")
	public ResponseEntity<List<SocioIngresoDTO>> obtenerTop3SociosSemana() {
		List<SocioIngresoDTO> resultados = historialService.obtenerTop3SociosSemanaActual();
		return ResponseEntity.ok(resultados);
	}

	@Operation(summary = "Obtener top 3 de los parqueaderos de la semana", description = "Endpoint que permite la obtencion del top 3 de parqueaderos con mas ganancias hechas en la semana vigente")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/top3-parqueaderos-ganancia-semana")
	public ResponseEntity<List<ParqueaderoGananciaDTO>> getTop3ParqueaderosGananciaSemana() {
		List<ParqueaderoGananciaDTO> listParqueaderosSemana = historialService.obtenerTop3ParqueaderosGananciaSemana();
		return ResponseEntity.ok(listParqueaderosSemana);

	}

}
