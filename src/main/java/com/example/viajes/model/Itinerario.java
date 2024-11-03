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

    //Aqui usamos la formula de haversine para calcular la distancia
    public Double getDistancia() {
        //Excepcion
        if (origen == null || destino == null) {
            throw new RuntimeException("Origen o destino no establecidos");
        }
        double latOrigen = Math.toRadians(origen.getLatitud());
        double lonOrigen = Math.toRadians(origen.getLongitud());
        double latDestino = Math.toRadians(destino.getLatitud());
        double lonDestino = Math.toRadians(destino.getLongitud());
        //radio de la tierra 
        final double RADIO_TIERRA = 6371.0;
        //Diferencias de latitud y de longitud 
        double deltaLat = latDestino - latOrigen;
        double deltaLon = lonDestino - lonDestino;

        //Haversine formula
        double a = Math.sin(deltaLat /2) * Math.sin(deltaLat /2) 
                + Math.cos(latOrigen) * Math.cos(latDestino) 
                * Math.sin(deltaLon /2) * Math.sin(deltaLon /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distancia = RADIO_TIERRA * c;
        return distancia;
    }
}
