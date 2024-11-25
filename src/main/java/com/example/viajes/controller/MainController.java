package com.example.viajes.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import com.example.viajes.Main;
import com.example.viajes.model.Itinerario;
import com.example.viajes.model.Provincia;

import javafx.application.Platform;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class MainController {
    private volatile  boolean activeThread = true;
    private Itinerario modelo;
    private Main vista;

    public Itinerario getModelo() {
        return modelo;
    }
    public void setModelo(Itinerario modelo) {
        this.modelo = modelo;
    }
    public Main getVista() {
        return vista;
    }
    public void setVista(Main vista) {
        this.vista = vista;
    }
    public void stopTableThread(){
        activeThread = false;
    }
    //Recupera las provincias de un fichero
    public ArrayList<Provincia> getProvinciasList(ArrayList<Provincia> provincias) throws IOException {
        File archivo = new File("src/main/java/com/example/viajes/model/provincias.txt");
        try (BufferedReader bf = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = bf.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length < 2) {
                    continue; // Aseguramos que el nombre de la provincia exista
                }
                String nombre = partes[0];
                // latitud y longitud se obtienen de la linea
                String[] coordenadas = partes[1].split(",");
                if (coordenadas.length < 2) {
                    continue; // Aseguramos que las coordenadas existan
                }

                // Otro try
                try {
                    double latitud = Double.parseDouble(coordenadas[0]);
                    double longitud = Double.parseDouble(coordenadas[1]);
                    Provincia provincia = new Provincia(nombre, latitud, longitud);
                    // Añadimos la provincia a la lista
                    provincias.add(provincia);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return provincias;
    }

  

    // Función para obtener la lista de provincias
    public ArrayList<Provincia> getProvinciasList() throws IOException {
        ArrayList<Provincia> provincias = new ArrayList<>();
        File archivo = new File("src/main/java/com/example/viajes/model/provincias.txt");
        try (BufferedReader bf = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = bf.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length < 2) {
                    continue; // Aseguramos que el nombre de la provincia exista
                }
                String nombre = partes[0];
                // latitud y longitud se obtienen de la linea
                String[] coordenadas = partes[1].split(",");
                if (coordenadas.length < 2) {
                    continue; // Aseguramos que las coordenadas existan
                }

                // Otro try
                try {
                    double latitud = Double.parseDouble(coordenadas[0]);
                    double longitud = Double.parseDouble(coordenadas[1]);
                    Provincia provincia = new Provincia(nombre, latitud, longitud);
                    // Añadimos la provincia a la lista
                    provincias.add(provincia);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return provincias;
    }

    
    //Metodo tabla
    public void getTableItinerario()throws IOException {
            
                //  validaciohn de los campos del formul,ario 
                  modelo.validacionTabla();

                  //si true
                  vista.setTable();

                
    }
    public void tableUpdateThread( TableView<Itinerario> tableItinerario) throws IOException {
        
        String fecha = LocalDate.now().toString();
        int contador = 0;
        //Orense va ser siempre la provincia de origen del bucle
        
        for (int i = 0; i < getProvinciasList().size(); i++) {
            if (getProvinciasList().get(i).getNombre().equalsIgnoreCase("Orense")) {
                //Orense = getProvinciasList().get(i);
                break; 
            }
            contador++;
        }
        Provincia Orense = getProvinciasList().get(contador);
        
        if (Orense == null) {
            System.out.println("Provincia de Orense no encontrada.");
            return;
        }
        Random random = new Random();
        //Actualiza la tabla cada  segundos con rutas de ourense a otras provincias
        new Thread(() -> {
        while (activeThread == true) {
            try {
                Platform.runLater(()->{
                    tableItinerario.getItems().clear();
                    
                        try {
                            for (int i = 0; i < 4; i++) {
                                //Cogemos un destino random de las provincias
                            Provincia destino = getProvinciasList().get(random.nextInt(getProvinciasList().size()));
                                //Comprobamos que no sea Orense
                                if (destino.getNombre().equalsIgnoreCase("Orense")) {
                                    return;
                                }
                            tableItinerario.getItems().add(new Itinerario(destino, fecha, Orense, 1));
                            }
                            tableItinerario.refresh();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    
                });
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            
        }
      }).start();
    }
    public void generateItinerarioTicket(Itinerario itinerario) {
        AlertasController alertas = new AlertasController();
        String titulo = "BILLETE COMPRADO!";
        String headerText = "BILLETE COMPRADO!";
        String msg = "El billete con fecha " + itinerario.getFecha() + " ha sido comprado con exito!\n\nCodigo de identificacion: " + generateCodeIdentifier();
        alertas.alertaInformativa(msg, titulo, headerText);
    }
    public String generateCodeIdentifier(){
        String code = "";
        for (int i = 0; i < 10; i++) {
            code += (int) (Math.random() * 10);
        }
        return code;
    }
    public void onlyNumbers(TextField txtViajeros) {
        txtViajeros.setTextFormatter(new TextFormatter<String>(change ->{
            String nuevoTexto = change.getControlNewText();
            //Permite solo numeros y borrar
            if (nuevoTexto.matches("\\d*")) { // Solo permite números
                return change; 
            }
            return null;
        }));    
 
         
    }
    public void onlyLetters(TextField txtField) {
        txtField.setTextFormatter(new TextFormatter<String>(change -> {
            String nuevoTexto = change.getControlNewText();
            // Permitir solo letras (incluyendo espacio) o la cadena vacía para borrar
            if (nuevoTexto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) { // Incluye letras y espacios
                return change;
            }
            return null; // Rechaza el cambio si no es una letra o espacio
        }));
    }
    public DatePicker dateController(DatePicker datePicker) {
        //Comprobamos que la fecha no sea anterior a la actual y que el billete no
        //Se pueda comprar con mas de dos meses de antelacion
        datePicker.setDayCellFactory( picker -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                //Esto es la fecha de hoy 
                LocalDate today = LocalDate.now();
                //Este el limite de dos meses a partir de hoy
                LocalDate limitDate = today.plusMonths(2);
                //Deshabilitamos las fechas anteriores a la actual y despues de dos meses
                if (date.isBefore(today) || date.isAfter(limitDate)) {
                    setVisible(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
        return datePicker;
    }

    

}