module com.example.viajes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;
    requires javafx.web;
    
    
    
    


    opens com.example.viajes to javafx.fxml, lombok;
    exports com.example.viajes;
    exports com.example.viajes.model;
}