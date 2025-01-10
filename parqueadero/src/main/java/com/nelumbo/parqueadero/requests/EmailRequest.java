package com.nelumbo.parqueadero.requests;

public class EmailRequest {
	private String email;
	private String placa;
	private String mensaje;
	private String parqueaderoNombre;

	public EmailRequest() {
	}

	public EmailRequest(String email, String placa, String mensaje, String parqueaderoNombre) {
		this.email = email;
		this.placa = placa;
		this.mensaje = mensaje;
		this.parqueaderoNombre = parqueaderoNombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getParqueaderoNombre() {
		return parqueaderoNombre;
	}

	public void setParqueaderoNombre(String parqueaderoNombre) {
		this.parqueaderoNombre = parqueaderoNombre;
	}
}
