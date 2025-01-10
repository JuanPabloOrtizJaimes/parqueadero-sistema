package com.nelumbo.parqueadero.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.requests.LoginRequest;
import com.nelumbo.parqueadero.requests.RegistroRequest;
import com.nelumbo.parqueadero.services.AutenticacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Seccion para realizar acciones de Login y Registro de socios")
public class AutenticacionController {

	@Autowired
	private AutenticacionService authenticationService;

	@Operation(summary = "Login", description = "Endpoint que permite la obtencion del token para realizar acciones segun el rol")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		MensajeResponse mensajeResponse = authenticationService.authenticateAndGetToken(loginRequest.getEmail(),
				loginRequest.getPassword());
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", mensajeResponse.getMensaje()));

	}

	@Operation(summary = "Registro Socio", description = "Endpoint que permite al admin registrar nuevos socios")
	@PostMapping("/registro")
	public ResponseEntity<?> register(@RequestBody RegistroRequest registroRequest) {

		MensajeResponse mensajeResponse = authenticationService.registrarUsuarioSocio(registroRequest);
		if (mensajeResponse != null && mensajeResponse.getMensaje() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", mensajeResponse.getMensaje()));

		}
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("id", mensajeResponse.getId()));

	}
}
