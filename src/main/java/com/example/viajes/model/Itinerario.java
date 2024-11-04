package com.example.viajes.model;


public class Itinerario {

    private Provincia origen;
    private Provincia destino;
    private String fecha;
    private Double distancia;
    private int duracion;
    private double precio;

    public Itinerario() {
    }

    public Itinerario(Provincia destino, Double distancia, int duracion, String fecha, Provincia origen, double precio) {
        this.destino = destino;
        this.distancia = distancia;
        this.duracion = duracion;
        this.fecha = fecha;
        this.origen = origen;
        this.precio = precio;
    }

    public Itinerario(Provincia destino, String fecha, Provincia origen) {
        this.destino = destino;
        this.fecha = fecha;
        this.origen = origen;
    }

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
        double deltaLon = lonDestino - lonOrigen;

        //Haversine formula
        double a = Math.sin(deltaLat /2) * Math.sin(deltaLat /2) 
                + Math.cos(latOrigen) * Math.cos(latDestino) 
                * Math.sin(deltaLon /2) * Math.sin(deltaLon /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

         distancia = RADIO_TIERRA * c;
        return distancia;
    }

    public int getDuracion() {
        if (origen == null || destino == null) {
            throw new RuntimeException("Origen o destino no establecidos");
        }
         double distanciaKm = getDistancia();
         //Velocidad 
         final double VELOCIDAD = 100.0;

         //se calcula la duracion
         double duracionHoras = distanciaKm / VELOCIDAD;
         int duracionMin = (int) (duracionHoras * 60);

        return duracionMin;
    }

    public double getPrecio() {
        //Cuesta 3 € por km
        precio =(distancia / 100) *3;
        //Redondear a 2 decimales
        precio = Math.round(precio * 100.0) / 100.0;
        return precio;
    }

    public void setOrigen(Provincia origen) {
        this.origen = origen;
    }

    public void setDestino(Provincia destino) {
        this.destino = destino;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Provincia getOrigen() {
        return origen;
    }

    public Provincia getDestino() {
        return destino;
    }

    public String getFecha() {
        return fecha;
    }
    public String getOrigenNombre() {
        return origen != null ? origen.getNombre() : "";
    }
    
    public String getDestinoNombre() {
        return destino != null ? destino.getNombre() : "";
    }

}
