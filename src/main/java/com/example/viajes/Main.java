package com.example.viajes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
        //WEbView
        String URL = "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d13333323.036969548!2d-17.577018371966222!3d35.325710170526584!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xc42e3783261bc8b%3A0xa6ec2c940768a3ec!2zRXNwYcOxYQ!5e0!3m2!1ses!2ses!4v1731862664842!5m2!1ses!2ses";
        private final WebView webView = new WebView();
        private final WebEngine engine = webView.getEngine();
        
        //Boolean 
        private boolean activeThread = true;
        
        //Tablewiew
        private TableView<Itinerario> tableItinerario = new TableView();
    @Override
    public void start(Stage stage) throws IOException {
        provinciaList = getProvinciasList();
        engine.load(getClass().getResource("/html/map.html").toExternalForm());
        BorderPane root = new BorderPane();
        

        Scene scene = new Scene(setItemsGridPane(), 900 , 600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        scene.setRoot(setItemsGridPane());

        //controles de entrada de textos FALTAN MAS CONTROLES
        mainController.onlyLetters(txtOrigen);
        mainController.onlyLetters(txtDestino);
        mainController.onlyNumbers(txtViajeros);

        //Thread
        mainController.tableUpdateThread( tableItinerario);
        
        btnBuscar.setOnAction(e -> { 
            excepciones.notSetOriginOrDestinationOrDate(txtOrigen, txtDestino, datePicker, txtViajeros);
            excepciones.notSetOriginOrDestination(txtOrigen, txtDestino);
            excepciones.notSetViajeros(txtViajeros);
            mainController.stopTableThread();
            String origen = txtOrigen.getText();
            String destino = txtDestino.getText();
            String fecha = datePicker.getValue().toString();
            Provincia provinciaOrigen = new Provincia();
            Provincia provinciaDestino = new Provincia();
            int viajeros = Integer.parseInt(txtViajeros.getText());

            for (Provincia provincia : provinciaList) {
                if (provincia.getNombre().equals(origen)) {
                    provinciaOrigen = provincia;
                }
                if (provincia.getNombre().equals(destino)) {
                    provinciaDestino = provincia;
                }
            }

            Itinerario itinerario=null;
            
                itinerario = new Itinerario(provinciaDestino, fecha, provinciaOrigen, viajeros);
           
            mainController.setModelo(itinerario);
            mainController.setVista(this);

            int contador =0;
            try {
                mainController.getTableItinerario();
            } catch (IOException e1) {
                System.out.println("Error al cargar la tabla  "+e1.getMessage());
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
    public void setTable(){
         tableItinerario.getItems().clear();
               try {
                for (int i = 0; i < 4; i++) {
                    tableItinerario.getItems().add(mainController.getModelo());
                }
               } catch (Exception e) {
                System.out.println("Error al cargar la tabla"+e.getMessage());
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
    private  ArrayList<Provincia> getProvinciasList() throws IOException{

        ArrayList<Provincia> provincias = mainController.getProvinciasList();

        return provincias;
    }
    public GridPane setItemsGridPane() {
        GridPane gridPane = new GridPane();
    
        gridPane.setHgap(8); // Espacio horizontal entre elementos
        gridPane.setVgap(12); // Espacio vertical entre filas
        gridPane.setPadding(new Insets(20, 20, 20, 20)); // Márgenes del gridpane
    
        // Configurar columnas para un diseño más controlado
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20); // Ancho para etiquetas y el WebView
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(30); // Ancho para campos de texto
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(30); // Ancho para campos adicionales
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(20); // Espacio para botones u otros elementos
        gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
    
        // Configuración de etiquetas
        lblTitulo.setId("lblTitulo");
        lblTitulo.setAlignment(Pos.CENTER);
        lblTitulo.setPrefSize(300, 30); // Tamaño del título
    
        lblOrigen.setId("lblOrigen");
        lblOrigen.setMinWidth(80); // Ancho mínimo para que no se recorte
        lblOrigen.setAlignment(Pos.CENTER_RIGHT); // Alineación a la derecha
    
        lblDestino.setId("lblDestino");
        lblDestino.setMinWidth(80);
        lblDestino.setAlignment(Pos.CENTER_RIGHT);
    
        // Configuración de campos de texto
        txtOrigen.setId("txtOrigen");
        txtOrigen.setPromptText("Introduce el origen");
        txtOrigen.setPrefWidth(200);
        txtOrigen.addEventFilter(KeyEvent.KEY_RELEASED, e -> showsuggestion(txtOrigen.getText()));

    
        txtDestino.setId("txtDestino");
        txtDestino.setPromptText("Introduce el destino");
        txtDestino.setPrefWidth(200);
        txtDestino.addEventFilter(KeyEvent.KEY_RELEASED, e -> showsuggestionDestino(txtDestino.getText()));

    
        txtViajeros.setId("txtViajeros");
        txtViajeros.setPromptText("Nº Viajeros");
        txtViajeros.setMaxWidth(100);
    
        datePicker.setId("datePicker");
        datePicker.setPromptText("Fecha");
        datePicker.setEditable(false);
    
        // Configuración de botones
        btnBuscar.setId("btnBuscar");
        btnBuscar.setText("Buscar");
    
        btnReservar.setId("btnReservar");
        btnReservar.setText("Reservar");
    
        // Configuración del WebView
        webView.setId("webView");
        webView.setPrefSize(300, 400); // Tamaño del WebView
    
        // Configuración de tabla
        tableItinerario.setId("tableItinerario");
        tableItinerario.setPlaceholder(new Label("No se encontraron resultados"));
        tableItinerario.setMaxHeight(150);
        tableItinerario.setMinWidth(400);
        
   if (tableItinerario.getColumns().isEmpty()) {
         // Añadir columnas a la tabla
         TableColumn<Itinerario, String> origenColumn = new TableColumn<>("Origen");
         origenColumn.setCellValueFactory(new PropertyValueFactory<>("origenNombre"));
     
         TableColumn<Itinerario, String> destinoColumn = new TableColumn<>("Destino");
         destinoColumn.setCellValueFactory(new PropertyValueFactory<>("destinoNombre"));
     
         TableColumn<Itinerario, String> fechaColumn = new TableColumn<>("Fecha");
         fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
     
         TableColumn<Itinerario, String> duracionColumn = new TableColumn<>("Duración (minutos)");
         duracionColumn.setMinWidth(150);
         duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));
     
         TableColumn<Itinerario, String> precioColumn = new TableColumn<>("Precio (€)");
         precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
     
         TableColumn<Itinerario, String> horaColumn = new TableColumn<>("Hora");
         horaColumn.setCellValueFactory(new PropertyValueFactory<>("hora"));
     
         tableItinerario.getColumns().addAll(origenColumn, destinoColumn, fechaColumn, duracionColumn, precioColumn, horaColumn);
   }
    
        // Añadir elementos al GridPane
        gridPane.add(lblTitulo, 1, 0, 3, 1); // Título centrado en la parte superior
        gridPane.add(webView, 0, 1, 1, 5); // WebView a la izquierda, ocupando varias filas
        gridPane.add(lblOrigen, 1, 1); // Etiqueta Origen
        gridPane.add(txtOrigen, 2, 1); // Campo Origen
        gridPane.add(lblDestino, 1, 2); // Etiqueta Destino
        gridPane.add(txtDestino, 2, 2); // Campo Destino
        gridPane.add(datePicker, 3, 1); // DatePicker en la misma fila de Origen
        gridPane.add(txtViajeros, 3, 2); // Campo Viajeros en la misma fila de Destino
        gridPane.add(btnBuscar, 2, 3); // Botón Buscar centrado en una fila nueva
        gridPane.add(tableItinerario, 1, 4, 2, 1); // Tabla ocupa varias columnas
        gridPane.add(btnReservar, 3, 5); // Botón Reservar al final, debajo de la tabla
    
        return gridPane;
    }
    
    public static void main(String[] args) {
        launch();
    }

    
}