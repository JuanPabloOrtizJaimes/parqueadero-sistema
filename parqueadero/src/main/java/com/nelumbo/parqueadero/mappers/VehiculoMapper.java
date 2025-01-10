package com.nelumbo.parqueadero.mappers;

import org.mapstruct.Mapper;

import com.nelumbo.parqueadero.dtos.VehiculoDTO;
import com.nelumbo.parqueadero.entities.Vehiculo;

@Mapper(componentModel = "spring")
public interface VehiculoMapper {

	VehiculoDTO paraDto(Vehiculo vehiculo);

	
}
