package com.example.viajes.view;

import javafx.scene.control.Alert;

public class AlertView {
    //String msg = "El usuario y password no son correctos";

    public void showAlertaInformativaClick(String msg,String titulo, String headerText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(headerText);
        alert.setContentText(msg);
        alert.showAndWait();
    }
        
   }
   