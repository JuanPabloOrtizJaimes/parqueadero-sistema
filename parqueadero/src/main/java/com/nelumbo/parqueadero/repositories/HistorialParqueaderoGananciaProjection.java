package com.nelumbo.parqueadero.repositories;

import java.math.BigDecimal;

import com.nelumbo.parqueadero.entities.Parqueadero;

public interface HistorialParqueaderoGananciaProjection {

	Parqueadero getParqueadero();

	BigDecimal getTotalGanancia();

}
