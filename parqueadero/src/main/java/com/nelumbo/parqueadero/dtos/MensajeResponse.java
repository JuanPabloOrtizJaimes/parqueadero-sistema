package com.nelumbo.parqueadero.dtos;

public class MensajeResponse {

	private Long id;
	private String mensaje;

	public MensajeResponse() {
	}

	public MensajeResponse(Long id) {
		this.id = id;
	}

	public MensajeResponse(String mensaje) {
		this.mensaje = mensaje;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
