package com.example.viajes.controller;


import com.example.viajes.view.AlertView;

import javafx.scene.control.Alert;

public class AlertasController {

    AlertView alertView = new AlertView();

    public void showAlertaInformativaClick(String sms) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText(null);
        alert.setContentText(sms);
        alert.showAndWait();
    }
}
