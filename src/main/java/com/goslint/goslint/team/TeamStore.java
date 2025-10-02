package com.goslint.goslint.team;

import java.util.List;
import java.util.Optional;

public interface TeamStore {
    Team save(Team team);
    List<Team> findAll();
    Optional<Team> findById(Long id);
    Optional<Team> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    void deleteById(Long id);
}
