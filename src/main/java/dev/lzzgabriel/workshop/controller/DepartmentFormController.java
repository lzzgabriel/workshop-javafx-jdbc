package dev.lzzgabriel.workshop.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dev.lzzgabriel.workshop.db.DbException;
import dev.lzzgabriel.workshop.listeners.DataChangeListener;
import dev.lzzgabriel.workshop.model.entities.Department;
import dev.lzzgabriel.workshop.model.services.DepartmentService;
import dev.lzzgabriel.workshop.util.Alerts;
import dev.lzzgabriel.workshop.util.Constraints;
import dev.lzzgabriel.workshop.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
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
  
  private DepartmentService service;
  
  private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
  
  @FXML
  public void onBtnSaveAction(ActionEvent event) {
    entity = getFormData();
    if (service == null) {
      throw new IllegalStateException("DepartmentService not available: not set");
    }
    try {
      service.saveOrUpdate(entity);
      notifyListeners();
    } catch (DbException e) {
      Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
    }
    Utils.currentStage(event).close();
  }
  
  private void notifyListeners() {
    dataChangeListeners.forEach(DataChangeListener::onDataChanged);
  }

  @FXML
  public void onBtnCancelAction(ActionEvent event) {
    Utils.currentStage(event).close();
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
  
  public Department getFormData() {
    var obj = new Department();
    
    obj.setId(Utils.tryParseToInt(txtID.getText()));
    obj.setName(txtName.getText());
    
    return obj;
  }
  
  public void addDataChangeListener(DataChangeListener listener) {
    dataChangeListeners.add(listener);
  }

  public void setDepartment(Department entity) {
    this.entity = entity;
  }

  public void setService(DepartmentService service) {
    this.service = service;
  }

}
