package com.nelumbo.parqueadero.exceptions;

public class DatosNoEncontradosException extends RuntimeException {

	private static final long serialVersionUID = -3207240990456648806L;

	public DatosNoEncontradosException(String mensaje) {
		super(mensaje);
	}
}
