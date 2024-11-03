package com.example.viajes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Itinerario {

    private Provincia origen;
    private Provincia destino;
    private String fecha;
    private Double distancia;

    public Double getDistancia() {
        //Excepcion
        if (origen == null || destino == null) {
            throw new RuntimeException("Origen o destino no establecidos");
        }
        double latOrigen = Math.toRadians(origen.getLatitud());
        double lonOrigen = Math.toRadians(origen.getLongitud());
        double latDestino = Math.toRadians(destino.getLatitud());
        double lonDestino = Math.toRadians(destino.getLongitud());
        return distancia;
    }
}
