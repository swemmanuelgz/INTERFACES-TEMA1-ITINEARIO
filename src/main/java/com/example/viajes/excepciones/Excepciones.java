package com.example.viajes.excepciones;

import com.example.viajes.controller.AlertasController;
import com.example.viajes.model.Itinerario;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Excepciones {
    private AlertasController alertas = new AlertasController();
    //excepcion por si no establecemos el origen o destino ni fecha
    public  void notSetOriginOrDestinationOrDate(TextField origen , TextField destino , DatePicker fecha,TextField viajeros) {
        if (origen.getText().isEmpty() || destino.getText().isEmpty() || fecha.getValue() == null || viajeros.getText().isEmpty()) {
            //alertas.showAlertaInformativaClick("Origen o destino no establecidos");
            alertas.alertaInformativa("Origen o destino no establecidos", "Error", "Error de Datos");
            throw new RuntimeException("Origen o destino no establecidos");

            
        }
    }
    //Excepcion no se pueden poner numeros en el campo de texto de origen o destino 
    public  void notSetOriginOrDestination(TextField origen , TextField destino) {
        if (origen.getText().matches("\\d+") || destino.getText().matches("\\d+")) {
            alertas.alertaInformativa("No se pueden introducir números en el campo de texto de origen o destino", "Error", "Error de datos");
            throw new RuntimeException("No se pueden introducir números en el campo de texto de origen o destino");
        }
    }
    //Excepcion no se pueden poner letras en el txt viajeros 
    public  void notSetViajeros(TextField viajeros) {
        if (viajeros.getText().matches("[a-zA-Z]+")) {
            alertas.alertaInformativa("No se pueden introducir letras en el campo de texto de viajeros", "Error", "Error de datos");
            throw new RuntimeException("No se pueden introducir letras en el campo de texto de viajeros");
        }
    }
    //Excepcion por si se pulsa el noton de reserva sin que hayan datos en la tabla
    public  void noDataInTable(Itinerario itinerario) {
        if (itinerario == null) {
            alertas.alertaInformativa("No hay datos en la tabla", "Error", "Error de datos");
        }
        throw new RuntimeException("No hay datos en la tabla");
    }
    
}
