package com.nelumbo.parqueadero.requests;

import com.nelumbo.parqueadero.entities.Rol;

import jakarta.validation.constraints.NotNull;

public class UsuarioRequest {

	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotNull
	private Rol rol;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}
