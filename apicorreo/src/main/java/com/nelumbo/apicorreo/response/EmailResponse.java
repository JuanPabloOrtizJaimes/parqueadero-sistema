package com.nelumbo.apicorreo.response;

public class EmailResponse {
	private String mensaje;

	public EmailResponse() {
	}

	public EmailResponse(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
