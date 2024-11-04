package com.example.viajes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.viajes.model.Itinerario;
import com.example.viajes.model.Provincia;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Main extends Application {
        //popup
        private Popup sugestiones = new Popup();
        //Lista
        private List<Provincia> provinciaList = new ArrayList<>();
        //TexztFields
        private TextField txtOrigen = new TextField();
        private TextField txtDestino = new TextField();
        //Labels 
        private Label lblTitulo = new Label("BUSCAR ITINERARIO");
        private Label lblOrigen = new Label("Origen");
        private Label lblDestino = new Label("Destino");
        //DatePicker
        private DatePicker datePicker = new DatePicker();
        //Buttons
        private Button btnBuscar = new Button("Buscar");
        //Imagen mapa de españa 
        private Image image = new Image(getClass().getResource("img/mapa_spain.png").toExternalForm());
        private ImageView imgMapa = new ImageView(image);
        
        //Tablewiew
        private TableView<Itinerario> tableItinerario = new TableView();
    @Override
    public void start(Stage stage) throws IOException {
        provinciaList = getProvinciasList();

        BorderPane root = new BorderPane();
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 900 , 700);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        //Ids de elementos para el CSS
        gridPane.setId("gridPane");
        lblTitulo.setId("lblTitulo");
        lblOrigen.setId("lblOrigen");
        lblDestino.setId("lblDestino");
        txtOrigen.setId("txtOrigen");
        txtDestino.setId("txtDestino");
        datePicker.setId("datePicker");
        btnBuscar.setId("btnBuscar");
        imgMapa.setId("imgMapa");
        tableItinerario.setId("tableItinerario");
        //Images 
        imgMapa.setFitHeight(250);
        imgMapa.setFitWidth(250);
        //TextFields
        txtOrigen.setPromptText("Origen");
        txtOrigen.addEventFilter(KeyEvent.KEY_RELEASED, e -> showsuggestion(txtOrigen.getText()));
        txtDestino.setPromptText("Destino");
        txtDestino.addEventFilter(KeyEvent.KEY_RELEASED, e -> showsuggestionDestino(txtDestino.getText()));
        //Labels
        lblOrigen.setText("Origen");
        lblDestino.setText("Destino");
        //DatePicker
        datePicker.setPromptText("Fecha");
        //Buttons
        btnBuscar.setText("Buscar");
        //Tablewiew
        tableItinerario.setPlaceholder(new Label("No se encontraron resultados"));
        //Columnas de la tabla 
        TableColumn<Itinerario, String> origenColumn = new TableColumn<>("Origen");
        origenColumn.setCellValueFactory(new PropertyValueFactory<>("origenNombre"));
        TableColumn<Itinerario, String> destinoColumn = new TableColumn<>("Destino");
        destinoColumn.setCellValueFactory(new PropertyValueFactory<>("destinoNombre"));
        TableColumn<Itinerario, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Itinerario, String> duracionColumn = new TableColumn<>("Duracion (minutos)");
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        TableColumn<Itinerario, String> precioColumn = new TableColumn<>("Precio (€)");
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));	
        //Añadir columnas a la tabla
        tableItinerario.getColumns().addAll(origenColumn, destinoColumn, fechaColumn, duracionColumn, precioColumn);
        //Poner elementos en la gridPane(5 columnas va tener para poder poner las cosas bien)
        gridPane.add(lblTitulo, 2, 0);
        gridPane.add(imgMapa, 0, 4);
        gridPane.add(lblOrigen,0,2 );
        gridPane.add(txtOrigen, 1, 2); //los lbl y txt en la misma fila
        gridPane.add(lblDestino, 2, 2);
        gridPane.add(txtDestino, 3, 2);
        gridPane.add(datePicker, 4, 2);//date picker tambien en la misma linea
        gridPane.add(btnBuscar, 2, 3); //El boton abajo en el medio
        gridPane.add(tableItinerario, 2, 4);
        
        scene.setRoot(gridPane);


        btnBuscar.setOnAction(e -> {
            String origen = txtOrigen.getText();
            String destino = txtDestino.getText();
            String fecha = datePicker.getValue().toString();
            Provincia provinciaOrigen = new Provincia();
            Provincia provinciaDestino = new Provincia();
            int contador =0;

            
                try {
                    for (int i = 0; i < getProvinciasList().size(); i++) {
                        //Conseguimos el objeto de origen
                        if (getProvinciasList().get(i).getNombre().toLowerCase().equals(origen.toLowerCase())) {
                            provinciaOrigen = getProvinciasList().get(i);
                            contador++;
                        }
                        //Ahora el destino 
                        if (getProvinciasList().get(i).getNombre().toLowerCase().equals(destino.toLowerCase())) {
                            provinciaDestino = getProvinciasList().get(i);
                            contador++;
                        }
                        if (contador==2) {
                            //Rompemos el bucle al construir los dos objetos 
                            break;
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            

            //tableItinerario.getItems().clear();
            tableItinerario.getItems().add(new Itinerario(provinciaDestino, fecha, provinciaOrigen));
        });

        stage.setTitle("Itinerario");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void showsuggestion(String input ){
        
        if (input.isEmpty()) {
            sugestiones.hide();
            return;
        }
        //Filtra las provincias 
        List<String> provinciasFiltradas = provinciaList.stream()
            .map(Provincia::getNombre)
            .filter(nombre -> nombre.toLowerCase().contains(input.toLowerCase()))
            .collect(Collectors.toList());

        if (provinciasFiltradas.isEmpty()) {
            sugestiones.hide();
            return;
        }
        //Creamos un ListView 
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(provinciasFiltradas);
        listView.setItems(items);

        listView.setOnMouseClicked(Event ->{
            if (!listView.getSelectionModel().isEmpty()) {
                String selected = listView.getSelectionModel().getSelectedItem();
                txtOrigen.setText(selected);
                sugestiones.hide();
            }
        });

            //tamaño del popup
            listView.setPrefHeight(Math.min(provinciasFiltradas.size(), 5)*24); //maximo  elementos muestra
            sugestiones.getContent().clear();
            sugestiones.getContent().add(listView);

            if (!sugestiones.isShowing()) {
                sugestiones.show(txtOrigen,
                txtOrigen.localToScreen(txtOrigen.getBoundsInLocal()).getMinX(),
                txtOrigen.localToScreen(txtOrigen.getBoundsInLocal()).getMaxY());
            }
        
    }
    private void showsuggestionDestino(String input ){
        
        if (input.isEmpty()) {
            sugestiones.hide();
            return;
        }
        //Filtra las provincias 
        List<String> provinciasFiltradas = provinciaList.stream()
            .map(Provincia::getNombre)
            .filter(nombre -> nombre.toLowerCase().contains(input.toLowerCase()))
            .collect(Collectors.toList());

        if (provinciasFiltradas.isEmpty()) {
            sugestiones.hide();
            return;
        }
        //Creamos un ListView 
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(provinciasFiltradas);
        listView.setItems(items);

        listView.setOnMouseClicked(Event ->{
            if (!listView.getSelectionModel().isEmpty()) {
                String selected = listView.getSelectionModel().getSelectedItem();
                txtDestino.setText(selected);
                sugestiones.hide();
            }
        });

            //tamaño del popup
            listView.setPrefHeight(Math.min(provinciasFiltradas.size(), 5)*24); //maximo  elementos muestra
            sugestiones.getContent().clear();
            sugestiones.getContent().add(listView);

            if (!sugestiones.isShowing()) {
                sugestiones.show(txtOrigen,
                txtDestino.localToScreen(txtDestino.getBoundsInLocal()).getMinX(),
                txtDestino.localToScreen(txtDestino.getBoundsInLocal()).getMaxY());
            }
        
    }
    //con esto obtenemos las provincias del fichero
    public ArrayList<Provincia> getProvinciasList() throws IOException{
        ArrayList<Provincia> provincias = new ArrayList<>();
        File archivo = new File("src/main/java/com/example/viajes/model/provincias.txt");
        try(BufferedReader bf = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = bf.readLine()) != null) {
                String [] partes =linea.split(":");
                if (partes.length <2) {
                    continue; //Aseguramos que el nombre de la provincia exista
                }
                String nombre = partes[0];
                //latitud y longitud se obtienen de la linea
                String[] coordenadas =  partes[1].split(",");
                if (coordenadas.length <2) {
                    continue; //Aseguramos que las coordenadas existan
                }

                //Otro try 
                try {
                    double latitud = Double.parseDouble(coordenadas[0]);
                    double longitud = Double.parseDouble(coordenadas[1]);
                    Provincia provincia = new Provincia(nombre, latitud, longitud);
                    //Añadimos la provincia a la lista 
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
    public static void main(String[] args) {
        launch();
    }
}