package com.nelumbo.parqueadero.services;

import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.requests.RegistroRequest;

public interface AutenticacionService {

	public MensajeResponse registrarUsuarioSocio(RegistroRequest registroRequest);

	public MensajeResponse authenticateAndGetToken(String email, String password);

}
