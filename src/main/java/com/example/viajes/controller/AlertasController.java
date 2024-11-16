package com.example.viajes.controller;


import com.example.viajes.view.AlertView;

public class AlertasController {

    AlertView alertView = new AlertView();

    
    public void alertaInformativa(String msg, String titulo, String headerText) {
        alertView.showAlertaInformativaClick(msg, titulo, headerText);
    }
}
