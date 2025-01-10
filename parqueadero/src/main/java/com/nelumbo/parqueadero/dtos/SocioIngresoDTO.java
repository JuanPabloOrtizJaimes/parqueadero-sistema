package com.nelumbo.parqueadero.dtos;

public class SocioIngresoDTO {
	private Long socioId;
	private String socioNombre;
	private Long cantidadVehiculos;

	public SocioIngresoDTO(Long socioId, String socioNombre, Long cantidadVehiculos) {
		this.socioId = socioId;
		this.socioNombre = socioNombre;
		this.cantidadVehiculos = cantidadVehiculos;
	}

	public Long getSocioId() {
		return socioId;
	}

	public void setSocioId(Long socioId) {
		this.socioId = socioId;
	}

	public String getSocioNombre() {
		return socioNombre;
	}

	public void setSocioNombre(String socioNombre) {
		this.socioNombre = socioNombre;
	}

	public Long getCantidadVehiculos() {
		return cantidadVehiculos;
	}

	public void setCantidadVehiculos(Long cantidadVehiculos) {
		this.cantidadVehiculos = cantidadVehiculos;
	}

}
