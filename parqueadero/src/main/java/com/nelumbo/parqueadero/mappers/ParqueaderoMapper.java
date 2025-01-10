package com.nelumbo.parqueadero.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nelumbo.parqueadero.dtos.ParqueaderoDTO;
import com.nelumbo.parqueadero.dtos.UsuarioDTO;
import com.nelumbo.parqueadero.entities.Parqueadero;
import com.nelumbo.parqueadero.entities.Usuario;
import com.nelumbo.parqueadero.requests.ParqueaderoRequest;

@Mapper(componentModel = "spring")
public interface ParqueaderoMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "socio", ignore = true)
	@Mapping(target = "vehiculos", ignore = true)
	Parqueadero paraEntidad(ParqueaderoRequest parqueaderoRequest);

    @Mapping(target = "socioDTO", source = "socio") 
	ParqueaderoDTO paraDto(Parqueadero parqueadero);

	UsuarioDTO paraUsuarioDto(Usuario usuario);

}
