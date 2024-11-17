package com.example.viajes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.viajes.controller.AlertasController;
import com.example.viajes.controller.MainController;
import com.example.viajes.excepciones.Excepciones;
import com.example.viajes.model.Itinerario;
import com.example.viajes.model.Provincia;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.ColumnConstraints;
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
        //Controladoras
        private MainController mainController = new MainController();
        private  AlertasController alertasController = new AlertasController();
        //Controladora de Excepciones 
        Excepciones excepciones = new Excepciones();
        //popup
        private Popup sugestiones = new Popup();
        //Lista
        private List<Provincia> provinciaList = new ArrayList<>();
        //TexztFields
        private final  TextField txtOrigen = new TextField();
        private final TextField txtDestino = new TextField();
        private final TextField txtViajeros = new TextField();
        //Labels 
        private final Label lblTitulo = new Label("ITINERARIO");
        private final  Label lblOrigen = new Label("Origen");
        private final  Label lblDestino = new Label("Destino");
        //DatePicker
        private  DatePicker datePicker = new DatePicker();
        //Buttons
        private Button btnBuscar = new Button("Buscar");
        private Button btnReservar = new Button("Reservar");
        //Imagen mapa de españa 
        private final Image image = new Image(getClass().getResource("img/mapa_spain.png").toExternalForm());
        private final ImageView imgMapa = new ImageView(image);

        //Boolean 
        private boolean activeThread = false;
        
        //Tablewiew
        private TableView<Itinerario> tableItinerario = new TableView();
    @Override
    public void start(Stage stage) throws IOException {
        provinciaList = getProvinciasList();

        BorderPane root = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);

           
        
        
        //Ids de elementos para el CSS
        gridPane.setId("gridPane");
        lblTitulo.setId("lblTitulo");
        lblTitulo.setPrefSize(250, 20);
        lblOrigen.setId("lblOrigen");
        lblDestino.setId("lblDestino");
        txtDestino.setPrefSize(200, 20);
        txtOrigen.setId("txtOrigen");
        txtOrigen.setPrefSize(200, 20);
        txtDestino.setId("txtDestino");
        txtViajeros.setId("txtViajeros");
        txtViajeros.setMaxWidth(75);
        datePicker.setId("datePicker");
        btnBuscar.setId("btnBuscar");
        imgMapa.setId("imgMapa");
        tableItinerario.setId("tableItinerario");
        btnReservar.setId("btnReservar");
        //Images 
        imgMapa.setFitHeight(250);
        imgMapa.setFitWidth(250);
        //TextFields
        txtOrigen.setPromptText("Origen");
        txtOrigen.addEventFilter(KeyEvent.KEY_RELEASED, e -> showsuggestion(txtOrigen.getText()));
        txtDestino.setPromptText("Destino");
        txtDestino.addEventFilter(KeyEvent.KEY_RELEASED, e -> showsuggestionDestino(txtDestino.getText()));
        txtViajeros.setPromptText("Nº Viajeros");
        //Labels
        lblOrigen.setText("Origen");
        lblDestino.setText("Destino");
        //DatePicker
        datePicker.setPromptText("Fecha");
        datePicker.setEditable(false);
        //Buttons
        btnBuscar.setText("Buscar");
        //Tablewiew
        tableItinerario.setPlaceholder(new Label("No se encontraron resultados"));
        //Columnas de la tabla 
        tableItinerario.setMaxHeight(150);
        tableItinerario.setMinWidth(530);
        TableColumn<Itinerario, String> origenColumn = new TableColumn<>("Origen");
        origenColumn.setCellValueFactory(new PropertyValueFactory<>("origenNombre"));
        TableColumn<Itinerario, String> destinoColumn = new TableColumn<>("Destino");
        destinoColumn.setCellValueFactory(new PropertyValueFactory<>("destinoNombre"));
        TableColumn<Itinerario, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Itinerario, String> duracionColumn = new TableColumn<>("Duracion (minutos)");
        duracionColumn.setMinWidth(150);
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        TableColumn<Itinerario, String> precioColumn = new TableColumn<>("Precio (€)");
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<Itinerario, String> horaColumn = new TableColumn<>("H");
        horaColumn.setCellValueFactory(new PropertyValueFactory<>("hora"));	
        //Añadir columnas a la tabla
        tableItinerario.getColumns().addAll(origenColumn, destinoColumn, fechaColumn, duracionColumn, precioColumn,horaColumn);
        //Poner elementos en la gridPane(5 columnas va tener para poder poner las cosas bien)
        gridPane.add(lblTitulo, 2, 0);
        gridPane.add(imgMapa, 0, 4,2,5);
        gridPane.add(lblOrigen,0,2 );
        gridPane.add(txtOrigen, 1, 2); //los lbl y txt en la misma fila
        gridPane.add(lblDestino, 2, 2);
        gridPane.add(txtDestino, 3, 2);
        gridPane.add(datePicker, 4, 2);//date picker tambien en la misma linea
        gridPane.add(txtViajeros, 4, 3);
        gridPane.add(btnBuscar, 2, 3); //El boton abajo en el medio
        gridPane.add(tableItinerario, 2, 4, 2, 5);
        gridPane.add(btnReservar, 4,9);



        Scene scene = new Scene(gridPane, 900 , 600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        scene.setRoot(gridPane);

        //controles de entrada de textos
        mainController.onlyNumbers(txtViajeros);
        
        btnBuscar.setOnAction(e -> {
            excepciones.notSetOriginOrDestinationOrDate(txtOrigen, txtDestino, datePicker, txtViajeros);
            excepciones.notSetOriginOrDestination(txtOrigen, txtDestino);
            excepciones.notSetViajeros(txtViajeros);
            String origen = txtOrigen.getText();
            String destino = txtDestino.getText();
            String fecha = datePicker.getValue().toString();
            Provincia provinciaOrigen = new Provincia();
            Provincia provinciaDestino = new Provincia();
            int viajeros = Integer.parseInt(txtViajeros.getText());
            int contador =0;
            try {
                mainController.getTableItinerario(tableItinerario, origen, destino, fecha, provinciaOrigen, provinciaDestino, viajeros, getProvinciasList());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
               
        });
        btnReservar.setOnAction(e -> {
           Itinerario itinerario = tableItinerario.getSelectionModel().getSelectedItem();
           excepciones.noDataInTable(itinerario);
           mainController.generateItinerarioTicket(itinerario);
           
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
        ArrayList<Provincia> provincias = mainController.getProvinciasList();

        return provincias;
    }
    public static void main(String[] args) {
        launch();
    }

    
}