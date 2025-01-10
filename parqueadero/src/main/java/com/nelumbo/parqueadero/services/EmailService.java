package com.nelumbo.parqueadero.services;

import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.requests.EmailRequest;

public interface EmailService {

	 public MensajeResponse enviarCorreo(EmailRequest emailRequest);
	
}
