package com.example.viajes.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.viajes.model.Itinerario;
import com.example.viajes.model.Provincia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;

public class MainController {

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

    private void showsuggestionDestino(String input, ArrayList<Provincia> provinciaList, TextField txtDestino,
            TextField txtOrigen, Popup sugestiones) {

        if (input.isEmpty()) {
            sugestiones.hide();
            return;
        }
        // Filtra las provincias
        List<String> provinciasFiltradas = provinciaList.stream()
                .map(Provincia::getNombre)
                .filter(nombre -> nombre.toLowerCase().contains(input.toLowerCase()))
                .collect(Collectors.toList());

        if (provinciasFiltradas.isEmpty()) {
            sugestiones.hide();
            return;
        }
        // Creamos un ListView
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(provinciasFiltradas);
        listView.setItems(items);

        listView.setOnMouseClicked(Event -> {
            if (!listView.getSelectionModel().isEmpty()) {
                String selected = listView.getSelectionModel().getSelectedItem();
                txtDestino.setText(selected);
                sugestiones.hide();
            }
        });

        // tamaño del popup
        listView.setPrefHeight(Math.min(provinciasFiltradas.size(), 5) * 24); // maximo elementos muestra
        sugestiones.getContent().clear();
        sugestiones.getContent().add(listView);

        if (!sugestiones.isShowing()) {
            sugestiones.show(txtOrigen,
                    txtDestino.localToScreen(txtDestino.getBoundsInLocal()).getMinX(),
                    txtDestino.localToScreen(txtDestino.getBoundsInLocal()).getMaxY());
        }

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

    

    public void getTableItinerario(TableView<Itinerario> tableItinerario, String origen, String destino, String fecha,
            Provincia provinciaOrigen, Provincia provinciaDestino, int viajeros, ArrayList<Provincia> provinciasList) {
                int contador = 0;
                tableItinerario.getItems().clear();
                try {
                    for (int i = 0; i < getProvinciasList().size(); i++) {
                        // Conseguimos el objeto de origen
                        if (getProvinciasList().get(i).getNombre().toLowerCase().equals(origen.toLowerCase())) {
                            provinciaOrigen = getProvinciasList().get(i);
                            contador++;
                        }
                        // Ahora el destino
                        if (getProvinciasList().get(i).getNombre().toLowerCase().equals(destino.toLowerCase())) {
                            provinciaDestino = getProvinciasList().get(i);
                            contador++;
                        }
                        if (contador == 2) {
                            // Rompemos el bucle al construir los dos objetos
                            break;
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        
                // tableItinerario.getItems().clear();
                for (int i = 0; i < 4; i++) {
                    tableItinerario.getItems().add(new Itinerario(provinciaDestino, fecha, provinciaOrigen, viajeros));
                }
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
    public void onlyLetters(KeyEvent keyEvent,TextField txtOrigen) {
        char c = keyEvent.getCharacter().charAt(0);
        if (!Character.isLetter(c)) {
            keyEvent.consume();
        }
    }

    

}