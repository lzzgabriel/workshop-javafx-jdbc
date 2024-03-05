module dev.lzzgabriel.workshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires ch.vorburger.mariadb4j;
    requires ch.vorburger.exec;
    requires java.sql;

    opens dev.lzzgabriel.workshop to javafx.fxml;
    opens dev.lzzgabriel.workshop.controller to javafx.fxml;
    opens dev.lzzgabriel.workshop.util to javafx.fxml;
    opens dev.lzzgabriel.workshop.model.entities to javafx.fxml;
    
    exports dev.lzzgabriel.workshop;
    exports dev.lzzgabriel.workshop.controller;
    exports dev.lzzgabriel.workshop.util;
    exports dev.lzzgabriel.workshop.model.entities;
}
