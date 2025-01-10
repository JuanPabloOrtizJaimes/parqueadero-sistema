package com.nelumbo.apicorreo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.apicorreo.request.EmailRequest;
import com.nelumbo.apicorreo.response.EmailResponse;

@RestController
@RequestMapping("/correo")
public class SimulacionCorreoController {

	private static final Logger logger = LoggerFactory.getLogger(SimulacionCorreoController.class);

	@PostMapping
	public ResponseEntity<EmailResponse> enviarCorreo(@RequestBody EmailRequest emailRequest) {

		logger.info("Enviando correo a: " + emailRequest.getEmail());
		if (emailRequest.getPlaca() != null) {
			logger.info("Placa: " + emailRequest.getPlaca());
		}
		logger.info("Mensaje: " + emailRequest.getMensaje());
		logger.info("Parqueadero: " + emailRequest.getParqueaderoNombre());

		EmailResponse emailResponse = new EmailResponse("Correo Enviado");
		return ResponseEntity.ok(emailResponse);
	}
}
