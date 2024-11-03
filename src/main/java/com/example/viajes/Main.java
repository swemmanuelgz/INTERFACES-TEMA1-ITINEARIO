package com.example.viajes;

import java.io.IOException;

import com.example.viajes.model.Itinerario;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
        txtDestino.setPromptText("Destino");
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
        origenColumn.setCellValueFactory(new PropertyValueFactory<>("origen"));
        TableColumn<Itinerario, String> destinoColumn = new TableColumn<>("Destino");
        destinoColumn.setCellValueFactory(new PropertyValueFactory<>("destino"));
        TableColumn<Itinerario, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Itinerario, String> duracionColumn = new TableColumn<>("Duracion");
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        TableColumn<Itinerario, String> precioColumn = new TableColumn<>("Precio");
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
        //Añadir textFields a la gridPane

        stage.setTitle("Itinerario");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}