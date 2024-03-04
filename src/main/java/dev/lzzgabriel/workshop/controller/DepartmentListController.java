package dev.lzzgabriel.workshop.controller;

import java.net.URL;
import java.util.ResourceBundle;

import dev.lzzgabriel.workshop.App;
import dev.lzzgabriel.workshop.model.entities.Department;
import dev.lzzgabriel.workshop.model.services.DepartmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DepartmentListController implements Initializable {
  
  private DepartmentService departmentService;
  
  private ObservableList<Department> departments;
  
  @FXML
  private TableView<Department> tableView;
  
  @FXML
  private TableColumn<Department, Integer> idColumn;
  
  @FXML
  private TableColumn<Department, String> nameColumn;
  
  @FXML
  private Button newButton;
  
  @FXML
  public void onNewButtonAction() {
    System.out.println("CLick");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeNodes();
  }
  
  private void initializeNodes() {
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    
    var stage = (Stage) App.getScene().getWindow();
    tableView.prefHeightProperty().bind(stage.heightProperty());
  }

  public void setDepartmentService(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }
  
  public void updateTableView() {
    if (departmentService == null) {
      throw new IllegalStateException("Department service unavailable: not set");
    }
    var list = departmentService.findAll();
    
    departments = FXCollections.observableArrayList(list);
    
    tableView.setItems(departments);
  }

}
