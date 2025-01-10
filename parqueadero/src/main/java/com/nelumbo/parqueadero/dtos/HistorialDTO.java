package com.nelumbo.parqueadero.dtos;

public class HistorialDTO {

	private String placa;
	private Long count;

	public HistorialDTO(String placa, Long count) {
		this.placa = placa;
		this.count = count;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
