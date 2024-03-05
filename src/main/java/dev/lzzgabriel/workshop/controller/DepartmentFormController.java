package dev.lzzgabriel.workshop.controller;

import java.net.URL;
import java.util.ResourceBundle;

import dev.lzzgabriel.workshop.model.entities.Department;
import dev.lzzgabriel.workshop.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {
  
  @FXML
  private TextField txtID;
  
  @FXML
  private TextField txtName;
  
  @FXML
  private Label lbError;
  
  @FXML
  private Button btnSave;
  
  @FXML
  private Button btnCancel;
  
  private Department entity;
  
  @FXML
  public void onBtnSaveAction() {
    
  }
  
  @FXML
  public void onBtnCancelAction() {
    
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeNodes();
  }
  
  private void initializeNodes() {
    Constraints.setTextFieldMaxLength(txtName, 30);
  }
  
  public void updateFormData() {
    if (entity == null) {
      throw new IllegalStateException("Entity not available: not set");
    }
    txtID.setText(String.valueOf(entity.getId()));
    txtName.setText(entity.getName());
  }

  public void setDepartment(Department entity) {
    this.entity = entity;
  }

}
