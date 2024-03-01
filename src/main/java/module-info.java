module dev.lzzgabriel.workshop {
    requires javafx.controls;
    requires javafx.fxml;

    opens dev.lzzgabriel.workshop to javafx.fxml;
    exports dev.lzzgabriel.workshop;
}
