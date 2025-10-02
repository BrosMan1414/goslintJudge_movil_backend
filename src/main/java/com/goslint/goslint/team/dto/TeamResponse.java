package com.goslint.goslint.team.dto;

public class TeamResponse {
    private Long id;
    private String nombre;
    private String emailContacto;

    public TeamResponse() {}
    public TeamResponse(Long id, String nombre, String emailContacto) {
        this.id = id;
        this.nombre = nombre;
        this.emailContacto = emailContacto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }
}
