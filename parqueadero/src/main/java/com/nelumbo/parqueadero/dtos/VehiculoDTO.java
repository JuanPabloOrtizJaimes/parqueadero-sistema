package com.nelumbo.parqueadero.dtos;

import java.time.LocalDateTime;

public class VehiculoDTO {

	private Long id;
	private String placa;
	private LocalDateTime fechaIngreso;

	public VehiculoDTO(Long id, String placa, LocalDateTime fechaIngreso) {
		super();
		this.id = id;
		this.placa = placa;
		this.fechaIngreso = fechaIngreso;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public LocalDateTime getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDateTime fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

}
