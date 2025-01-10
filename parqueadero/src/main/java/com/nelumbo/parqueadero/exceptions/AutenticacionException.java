package com.nelumbo.parqueadero.exceptions;

public class AutenticacionException extends RuntimeException {

	private static final long serialVersionUID = 8040956835328138003L;

	public AutenticacionException(String mensaje) {
		super(mensaje);
	}

}
