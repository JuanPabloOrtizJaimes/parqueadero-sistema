package com.nelumbo.parqueadero.services;

import java.util.List;

import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.dtos.ParqueaderoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.requests.VehiculoRequest;

public interface VehiculoService {

	public MensajeResponse registrarVehiculo(VehiculoRequest vehiculoRequest);

	public MensajeResponse registrarSalidaVehiculo(VehiculoRequest vehiculoRequest);

	public List<ParqueaderoDTO> obtenerParqueaderosAsociados();

	public List<VehiculoDTO> obtenerVehiculosPorIdParqueaderoAsociado(Long id);

}
