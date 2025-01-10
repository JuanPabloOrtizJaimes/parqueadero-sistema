package com.nelumbo.parqueadero.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Parqueadero {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = false)
	private int capacidad;
	@Column(nullable = false)
	private double costoPorHora;
	@OneToMany(mappedBy = "parqueadero")
	private List<Vehiculo> vehiculos;

	@ManyToOne
	@JoinColumn(name = "socio_id")
	private Usuario socio;

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

	public Usuario getSocio() {
		return socio;
	}

	public void setSocio(Usuario socio) {
		this.socio = socio;
	}

	public List<Vehiculo> getVehiculos() {
		return vehiculos;
	}

	public void setVehiculos(List<Vehiculo> vehiculos) {
		this.vehiculos = vehiculos;
	}
	
	

}
