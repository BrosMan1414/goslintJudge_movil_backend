package com.goslint.goslint.team;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryTeamStore {
    private final Map<Long, Team> byId = new ConcurrentHashMap<>();
    private final Map<String, Long> nombreIndex = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);
    private final ObjectMapper mapper = new ObjectMapper();

    public InMemoryTeamStore(@Value("${app.teams.json-url:}") String jsonUrl) {
        if (jsonUrl != null && !jsonUrl.isBlank()) {
            try {
                loadFromUrl(jsonUrl);
            } catch (Exception e) {
                // log simple message; avoid failing app
                System.err.println("No se pudo cargar teams desde URL: " + e.getMessage());
            }
        }
    }

    private void loadFromUrl(String jsonUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(jsonUrl)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            List<Team> teams = mapper.readValue(response.body(), new TypeReference<List<Team>>(){});
            for (Team t : teams) {
                if (t.getId() == null) {
                    t.setId(seq.getAndIncrement());
                } else {
                    seq.set(Math.max(seq.get(), t.getId() + 1));
                }
                byId.put(t.getId(), t);
                if (t.getNombre() != null) {
                    nombreIndex.put(t.getNombre().toLowerCase(Locale.ROOT), t.getId());
                }
            }
        } else {
            throw new IOException("HTTP " + response.statusCode());
        }
    }

    // Métodos utilitarios (no implementan ninguna interfaz)
    public Team save(Team team) {
        if (team.getId() == null) {
            team.setId(seq.getAndIncrement());
        }
        byId.put(team.getId(), team);
        if (team.getNombre() != null) {
            nombreIndex.put(team.getNombre().toLowerCase(Locale.ROOT), team.getId());
        }
        return team;
    }

    public List<Team> findAll() { return new ArrayList<>(byId.values()); }

    public Optional<Team> findById(Long id) { return Optional.ofNullable(byId.get(id)); }

    public Optional<Team> findByNombreIgnoreCase(String nombre) {
        Long id = nombreIndex.get(nombre.toLowerCase(Locale.ROOT));
        return id == null ? Optional.empty() : Optional.ofNullable(byId.get(id));
    }

    public boolean existsByNombreIgnoreCase(String nombre) { return nombreIndex.containsKey(nombre.toLowerCase(Locale.ROOT)); }

    public void deleteById(Long id) {
        Team removed = byId.remove(id);
        if (removed != null && removed.getNombre() != null) {
            nombreIndex.remove(removed.getNombre().toLowerCase(Locale.ROOT));
        }
    }
}
