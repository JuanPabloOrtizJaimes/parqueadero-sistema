package com.nelumbo.parqueadero.dtos;

public class ParqueaderoDTO {

	private Long id;
	private String nombre;
	private int capacidad;
	private double costoPorHora;
	private UsuarioDTO socioDTO;

	public ParqueaderoDTO(Long id, String nombre, int capacidad, double costoPorHora, UsuarioDTO socioDTO) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.capacidad = capacidad;
		this.costoPorHora = costoPorHora;
		this.socioDTO = socioDTO;
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

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public double getCostoPorHora() {
		return costoPorHora;
	}

	public void setCostoPorHora(double costoPorHora) {
		this.costoPorHora = costoPorHora;
	}

	public UsuarioDTO getSocioDTO() {
		return socioDTO;
	}

	public void setSocioDTO(UsuarioDTO socioDTO) {
		this.socioDTO = socioDTO;
	}

}
