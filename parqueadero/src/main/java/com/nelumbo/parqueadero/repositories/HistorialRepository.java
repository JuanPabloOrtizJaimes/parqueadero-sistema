package com.nelumbo.parqueadero.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nelumbo.parqueadero.entities.Historial;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {

	@Query("SELECT h.placa AS placa, COUNT(placa) AS count FROM Historial h GROUP BY placa ORDER BY COUNT(placa) DESC")
	List<HistorialCountProjection> vehiculosConMasRegistrosEnParqueaderos(Pageable pageable);

	@Query("SELECT h.placa AS placa, COUNT(placa) AS count FROM Historial h WHERE h.parqueadero.id = :parqueaderoId GROUP BY placa ORDER BY COUNT(placa) DESC")
	List<HistorialCountProjection> vehiculosConMasRegistrosEnUnParqueadero(@Param("parqueaderoId") Long parqueaderoId,
			Pageable pageable);

	@Query("SELECT h.parqueadero AS parqueadero  , SUM(h.monto) AS totalGanancia FROM Historial h "
			+ "WHERE h.fechaIngreso >= :fechaInicioSemana AND h.fechaIngreso <= :fechaFinSemana "
			+ "GROUP BY parqueadero ORDER BY totalGanancia DESC")
	List<HistorialParqueaderoGananciaProjection> obtenerTop3ParqueaderosGananciaSemana(
			@Param("fechaInicioSemana") LocalDateTime fechaInicioSemana,
			@Param("fechaFinSemana") LocalDateTime fechaFinSemana, Pageable pageable);

	@Query("SELECT h.parqueadero AS parqueadero  , SUM(h.monto) AS totalGanancia FROM Historial h "
			+ "WHERE h.parqueadero.id = :parqueaderoId AND h.fechaIngreso >= :fechaHoy")
	HistorialParqueaderoGananciaProjection obtenerGananciasHoy(@Param("parqueaderoId") Long parqueaderoId,
			@Param("fechaHoy") LocalDateTime fechaHoy);

	@Query("SELECT h.parqueadero AS parqueadero  , SUM(h.monto) AS totalGanancia FROM Historial h "
			+ "WHERE h.parqueadero.id = :parqueaderoId AND h.fechaIngreso >= :fechaInicioSemana AND h.fechaIngreso <= :fechaFinSemana")
	HistorialParqueaderoGananciaProjection obtenerGananciasSemana(@Param("parqueaderoId") Long parqueaderoId,
			@Param("fechaInicioSemana") LocalDateTime fechaInicioSemana,
			@Param("fechaFinSemana") LocalDateTime fechaFinSemana);

	@Query("SELECT h.parqueadero AS parqueadero  , SUM(h.monto) AS totalGanancia FROM Historial h "
			+ "WHERE h.parqueadero.id = :parqueaderoId AND h.fechaIngreso >= :fechaInicioMes AND h.fechaIngreso <= :fechaFinMes")
	HistorialParqueaderoGananciaProjection obtenerGananciasMes(@Param("parqueaderoId") Long parqueaderoId,
			@Param("fechaInicioMes") LocalDateTime fechaInicioMes, @Param("fechaFinMes") LocalDateTime fechaFinMes);

	@Query("SELECT h.parqueadero AS parqueadero  , SUM(h.monto) AS totalGanancia FROM Historial h "
			+ "WHERE h.parqueadero.id = :parqueaderoId AND h.fechaIngreso >= :fechaInicioAnio AND h.fechaIngreso <= :fechaFinAnio")
	HistorialParqueaderoGananciaProjection obtenerGananciasAnio(@Param("parqueaderoId") Long parqueaderoId,
			@Param("fechaInicioAnio") LocalDateTime fechaInicioAnio, @Param("fechaFinAnio") LocalDateTime fechaFinAnio);

}
