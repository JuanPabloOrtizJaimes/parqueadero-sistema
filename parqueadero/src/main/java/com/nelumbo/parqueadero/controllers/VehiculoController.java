package com.nelumbo.parqueadero.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.dtos.ParqueaderoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.requests.VehiculoRequest;
import com.nelumbo.parqueadero.services.VehiculoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/vehiculo")
@Tag(name = "Vehiculo", description = "Seccion para realizar acciones registro y salidas de vehiculos,obtener parqueaderos asociados y vehiculos de un parqueadero asociado")
public class VehiculoController {

	@Autowired
	private VehiculoService vehiculoService;

	@Operation(summary = "Registrar vehiculo", description = "Endpoint que permite el registro de un vehiculo a un parqueadero")
	@PostMapping("/entrada")
	public ResponseEntity<?> registrarVehiculo(@RequestBody VehiculoRequest vehiculoRequest) {
		try {

			MensajeResponse nuevoIngreso = vehiculoService.registrarVehiculo(vehiculoRequest);
			if (nuevoIngreso != null && nuevoIngreso.getMensaje() != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", nuevoIngreso.getMensaje()));
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", nuevoIngreso.getId()));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error interno del servidor"));
		}

	}

	@Operation(summary = "Registrar salida de vehiculo", description = "Endpoint que permite el registro de la salida de un vehiculo del parqueadero")
	@PostMapping("/salida")
	public ResponseEntity<?> registrarSalidaVehiculo(@RequestBody VehiculoRequest vehiculoRequest) {
		try {

			MensajeResponse salida = vehiculoService.registrarSalidaVehiculo(vehiculoRequest);
			if (salida.getMensaje().equalsIgnoreCase("Salida registrada")) {
				return ResponseEntity.status(HttpStatus.OK).body(Map.of("mensaje", salida.getMensaje()));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", salida.getMensaje()));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("mensaje", "Error interno del servidor"));
		}

	}

	@Operation(summary = "Obtener parqueaderos asociados", description = "Endpoint que permite la obtencion de los parqueaderos del socio logueado")
	@GetMapping("/parqueaderosAsociados")
	public ResponseEntity<List<ParqueaderoDTO>> obtenerParqueaderosAsociados() {

		List<ParqueaderoDTO> parqueaderoDTOs = vehiculoService.obtenerParqueaderosAsociados();
		return ResponseEntity.status(HttpStatus.OK).body(parqueaderoDTOs);

	}

	@Operation(summary = "Obtener vehiculos de parqueadero asociado", description = "Endpoint que permite la obtencion de los vehiculos de un parqueadero del socio logueado")
	@GetMapping("/parqueadero/{id}/asociados")
	public ResponseEntity<List<VehiculoDTO>> obtenerVehiculosPorIdParqueaderoAsociado(@PathVariable Long id) {
		List<VehiculoDTO> vehiculoDTOs = vehiculoService.obtenerVehiculosPorIdParqueaderoAsociado(id);
		return ResponseEntity.status(HttpStatus.OK).body(vehiculoDTOs);

	}
}
