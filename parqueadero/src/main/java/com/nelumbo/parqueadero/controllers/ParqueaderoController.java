package com.nelumbo.parqueadero.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.parqueadero.dtos.ParqueaderoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.requests.ActualizarParqueaderoRequest;
import com.nelumbo.parqueadero.requests.ParqueaderoRequest;
import com.nelumbo.parqueadero.servicesimpl.ParqueaderoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/parqueadero")
@Tag(name = "Parqueadero", description = "Seccion para realizar acciones crud de parqueadero,ver detalle de vehiculos en parqueadero y vincular socios")
public class ParqueaderoController {

	@Autowired
	private ParqueaderoServiceImpl parqueaderoService;

	@Operation(summary = "Crear parqueadero", description = "Endpoint que permite el registro de un parqueadero")
	@PostMapping
	public ResponseEntity<ParqueaderoDTO> crearParqueadero(@RequestBody ParqueaderoRequest parqueaderoRequest) {
		ParqueaderoDTO parqueaderoDTO = parqueaderoService.crearParqueadero(parqueaderoRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(parqueaderoDTO);

	}

	@Operation(summary = "Obtener parqueaderos", description = "Endpoint que permite la obtencion de parqueaderos existentes")
	@GetMapping
	public ResponseEntity<List<ParqueaderoDTO>> obtenerParqueaderos() {
		List<ParqueaderoDTO> parqueaderoDTOs = parqueaderoService.obtenerParqueaderos();
		return ResponseEntity.status(HttpStatus.OK).body(parqueaderoDTOs);

	}

	@Operation(summary = "Obtener parqueadero", description = "Endpoint que permite la obtencion de un parqueadero existente")
	@GetMapping("/{id}")
	public ResponseEntity<ParqueaderoDTO> obtenerParqueaderoPorId(@PathVariable Long id) {
		Optional<ParqueaderoDTO> parqueaderoDTO = parqueaderoService.obtenerParqueaderoPorId(id);
		if (parqueaderoDTO.isPresent()) {
			return ResponseEntity.ok(parqueaderoDTO.get());
		}
		return ResponseEntity.notFound().build();
	}

	@Operation(summary = "Obtener vehiculos parqueadero", description = "Endpoint que permite la obtencion de  los vehiculos de un parqueadero existente")
	@GetMapping("/{id}/vehiculos")
	public ResponseEntity<List<VehiculoDTO>> obtenerVehiculosPorIdParqueadero(@PathVariable Long id) {
		List<VehiculoDTO> vehiculoDTOs = parqueaderoService.obtenerVehiculosPorIdParqueadero(id);
		if (vehiculoDTOs.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(vehiculoDTOs);
	}

	@Operation(summary = "Actualizar datos parqueadero", description = "Endpoint que permite la actualizacion de un parqueadero existente")
	@PutMapping("/{id}")
	public ResponseEntity<ParqueaderoDTO> actualizarParqueadero(@PathVariable Long id,
			@RequestBody ActualizarParqueaderoRequest actualizarParqueaderoRequest) {
		ParqueaderoDTO parqueaderoDTO = parqueaderoService.actualizarParqueadero(id, actualizarParqueaderoRequest);
		return ResponseEntity.status(HttpStatus.OK).body(parqueaderoDTO);

	}

	@Operation(summary = "Vincular socio", description = "Endpoint que permite la vinculacion de un socio a un parqueadero existente")
	@PutMapping("/{id}/vincular-socio")
	public ResponseEntity<ParqueaderoDTO> vincularSocioAParqueadero(@PathVariable Long id, @RequestParam String email) {
		try {
			ParqueaderoDTO parqueaderoActualizado = parqueaderoService.vincularSocioAParqueadero(id, email);
			return ResponseEntity.ok(parqueaderoActualizado);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Eliminar parqueadero", description = "Endpoint que permite borrar un parqueadero existente")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarParqueadero(@PathVariable Long id) {
		try {
			parqueaderoService.eliminarParqueadero(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
		}
	}

}
