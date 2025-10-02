package com.goslint.goslint.team;

import com.goslint.goslint.team.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamCreateRequest req) {
        TeamResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/teams/" + created.getId())).body(created);
    }

    @GetMapping
    public List<TeamResponse> list() { return service.list(); }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> get(@PathVariable Long id) {
        return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public TeamResponse update(@PathVariable Long id, @Valid @RequestBody TeamUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
