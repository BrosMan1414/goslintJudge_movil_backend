package com.goslint.goslint.team;

import com.goslint.goslint.team.dto.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamService {
    private final TeamRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }

    public TeamResponse create(TeamCreateRequest req) {
        if (repository.existsByNombreIgnoreCase(req.getNombre())) {
            throw new IllegalArgumentException("El nombre de equipo ya existe");
        }
        if (repository.existsByEmailContactoIgnoreCase(req.getEmailContacto())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        String hash = passwordEncoder.encode(req.getPassword());
        Team saved = repository.save(new Team(req.getNombre().trim(), req.getEmailContacto().trim(), hash));
        return toResponse(saved);
    }

    public List<TeamResponse> list() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public Optional<TeamResponse> get(Long id) {
        return repository.findById(id).map(this::toResponse);
    }

    public TeamResponse update(Long id, TeamUpdateRequest req) {
        Team team = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
        if (!team.getNombre().equalsIgnoreCase(req.getNombre()) && repository.existsByNombreIgnoreCase(req.getNombre())) {
            throw new IllegalArgumentException("El nombre de equipo ya existe");
        }
        team.setNombre(req.getNombre().trim());
        if (req.getEmailContacto() != null && !req.getEmailContacto().isBlank()) {
            team.setEmailContacto(req.getEmailContacto().trim());
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            team.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        }
        return toResponse(repository.save(team));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public boolean login(LoginRequest req) {
        return repository.findByNombreIgnoreCase(req.getNombre())
                .map(team -> {
                    String stored = team.getPasswordHash();
                    if (stored == null) return false;
                    String s = stored.trim();
                    boolean looksBcrypt = s.startsWith("$2a$") || s.startsWith("$2b$") || s.startsWith("$2y$");
                    if (looksBcrypt) {
                        return passwordEncoder.matches(req.getPassword(), s);
                    }
                    // Fallback: comparar texto plano (útil para datos de prueba no hasheados)
                    return s.equals(req.getPassword());
                })
                .orElse(false);
    }

    private TeamResponse toResponse(Team team) {
        return new TeamResponse(team.getId(), team.getNombre(), team.getEmailContacto());
    }
}
