package com.example.viajes.model;


public class Provincia {
    private String nombre;
    private Double latitud;
    private Double longitud;

    public Provincia() {
        
    }
    public Provincia(String nombre, Double latitud, Double longitud) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

}
