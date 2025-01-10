package com.nelumbo.parqueadero.dtos;

import java.math.BigDecimal;

public class ParqueaderoGananciaDTO {

	private Long id;
	private String nombre;
	private BigDecimal totalGanancia;

	public ParqueaderoGananciaDTO(Long id, String nombre, BigDecimal totalGanancia) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.totalGanancia = totalGanancia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getTotalGanancia() {
		return totalGanancia;
	}

	public void setTotalGanancia(BigDecimal totalGanancia) {
		this.totalGanancia = totalGanancia;
	}

}
