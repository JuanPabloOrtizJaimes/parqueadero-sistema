package com.nelumbo.parqueadero.services;

import java.util.List;
import java.util.Optional;

import com.nelumbo.parqueadero.dtos.ParqueaderoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.requests.ActualizarParqueaderoRequest;
import com.nelumbo.parqueadero.requests.ParqueaderoRequest;

public interface ParqueaderoService {

	public ParqueaderoDTO crearParqueadero(ParqueaderoRequest parqueaderoRequest);

	public List<ParqueaderoDTO> obtenerParqueaderos();

	public Optional<ParqueaderoDTO> obtenerParqueaderoPorId(Long id);

	public List<VehiculoDTO> obtenerVehiculosPorIdParqueadero(Long id);

	public ParqueaderoDTO actualizarParqueadero(Long id, ActualizarParqueaderoRequest actualizarParqueaderoRequest);

	public ParqueaderoDTO vincularSocioAParqueadero(Long id, String mail);

	public void eliminarParqueadero(Long id);
	
	
}
