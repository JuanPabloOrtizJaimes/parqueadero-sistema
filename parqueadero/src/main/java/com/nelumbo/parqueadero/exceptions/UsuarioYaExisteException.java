package com.nelumbo.parqueadero.exceptions;

public class UsuarioYaExisteException extends RuntimeException {

	private static final long serialVersionUID = -575430563063154616L;

	public UsuarioYaExisteException(String mensaje) {
		super(mensaje);
	}
}