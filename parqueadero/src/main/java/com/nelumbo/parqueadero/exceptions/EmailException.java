package com.nelumbo.parqueadero.exceptions;

public class EmailException extends RuntimeException {
	
	private static final long serialVersionUID = 2358335494927685080L;

	public EmailException(String mensaje) {
		super(mensaje);
	}
}
