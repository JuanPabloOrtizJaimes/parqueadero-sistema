package com.nelumbo.parqueadero.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nelumbo.parqueadero.entities.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

	Optional<Vehiculo> findByPlaca(String placa);

	@Query("SELECT v FROM Vehiculo v WHERE v.parqueadero.id = :parqueaderoId AND NOT EXISTS "
			+ "(SELECT h FROM Historial h WHERE h.placa = v.placa AND h.parqueadero.id = :parqueaderoId)")
	List<Vehiculo> vehiculosPrimerIngreso(@Param("parqueaderoId") Long parqueaderoId);

	@Query("""
			SELECT p.socio.id AS socioId, p.socio.nombre AS socioNombre, COUNT(v.id) AS cantidadVehiculos
			FROM Vehiculo v
			JOIN v.parqueadero p
			WHERE v.fechaIngreso BETWEEN :inicioSemana AND :finSemana
			GROUP BY p.socio.id, p.socio.nombre
			ORDER BY COUNT(v.id) DESC
			""")
	List<SocioIngresoProjection> top3SociosConMasVehiculos(@Param("inicioSemana") LocalDateTime inicioSemana,
			@Param("finSemana") LocalDateTime finSemana, Pageable pageable);
}
