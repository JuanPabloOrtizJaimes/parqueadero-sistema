package com.nelumbo.parqueadero.requests;

public class ParqueaderoRequest {

	private String nombre;
	private int capacidad;
	private double costoPorHora;

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

}
