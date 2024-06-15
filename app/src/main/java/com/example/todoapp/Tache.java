package com.example.todoapp;

import java.io.Serializable;

public class Tache implements Serializable {
    private String status;
    private String nom;
    private String description;

    public Tache(String status, String nom, String description){
        this.status = status;
        this.nom = nom;
        this.description =description;

    }

    public String getDescription() {
        return description;
    }

    public String getNom() {
        return nom;
    }

    public String getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
