package com.nelumbo.parqueadero.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nelumbo.parqueadero.entities.Parqueadero;

public interface ParqueaderoRepository extends JpaRepository<Parqueadero, Long> {

}
