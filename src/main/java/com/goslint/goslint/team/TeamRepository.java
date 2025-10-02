package com.goslint.goslint.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
	Optional<Team> findByNombreIgnoreCase(String nombre);
	boolean existsByNombreIgnoreCase(String nombre);
	boolean existsByEmailContactoIgnoreCase(String emailContacto);
}

