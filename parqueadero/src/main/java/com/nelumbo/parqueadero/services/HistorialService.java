package com.nelumbo.parqueadero.services;

import java.util.List;

import com.nelumbo.parqueadero.dtos.HistorialDTO;
import com.nelumbo.parqueadero.dtos.ParqueaderoGananciaDTO;
import com.nelumbo.parqueadero.dtos.SocioIngresoDTO;
import com.nelumbo.parqueadero.dtos.VehiculoDTO;

public interface HistorialService {

	List<HistorialDTO> vehiculosConMasRegistrosEnParqueaderos();

	List<HistorialDTO> vehiculosConMasRegistrosEnUnParqueadero(Long parqueaderoId);

	List<VehiculoDTO> vehiculosPrimerIngreso(Long parqueaderoId);

	List<ParqueaderoGananciaDTO> obtenerTop3ParqueaderosGananciaSemana();

	List<SocioIngresoDTO> obtenerTop3SociosSemanaActual();

	ParqueaderoGananciaDTO obtenerGananciasHoy(Long parqueaderoId);

	ParqueaderoGananciaDTO obtenerGananciasSemana(Long parqueaderoId);

	ParqueaderoGananciaDTO obtenerGananciasMes(Long parqueaderoId);

	ParqueaderoGananciaDTO obtenerGananciasAnio(Long parqueaderoId);

}
