package com.goslint.goslint.team.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TeamUpdateRequest {
    @NotBlank
    private String nombre;
    @Email
    private String emailContacto; // opcional
    private String password; // opcional

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
