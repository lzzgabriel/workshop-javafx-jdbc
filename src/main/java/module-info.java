module dev.lzzgabriel.workshop {
    requires javafx.controls;
    requires javafx.fxml;

    opens dev.lzzgabriel.workshop to javafx.fxml;
    opens dev.lzzgabriel.workshop.controller to javafx.fxml;
    opens dev.lzzgabriel.workshop.util to javafx.fxml;
    
    exports dev.lzzgabriel.workshop;
    exports dev.lzzgabriel.workshop.controller;
    exports dev.lzzgabriel.workshop.util;
}
