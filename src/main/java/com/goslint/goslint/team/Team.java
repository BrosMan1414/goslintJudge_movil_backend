package com.goslint.goslint.team;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "equipos")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nombre del equipo (no único en tu esquema, lo controlaremos a nivel app)
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Email
    @NotBlank
    @Column(name = "email_contacto", nullable = false, unique = true)
    private String emailContacto;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "puntaje", nullable = false)
    private Integer puntaje = 0;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private java.time.LocalDateTime fechaCreacion;

    public Team() {}

    public Team(String nombre, String emailContacto, String passwordHash) {
        this.nombre = nombre;
        this.emailContacto = emailContacto;
        this.passwordHash = passwordHash;
        this.puntaje = 0;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmailContacto() { return emailContacto; }
    public String getPasswordHash() { return passwordHash; }
    public Integer getPuntaje() { return puntaje; }
    public java.time.LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }
    public void setFechaCreacion(java.time.LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
