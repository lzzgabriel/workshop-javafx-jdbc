package dev.lzzgabriel.workshop.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dev.lzzgabriel.workshop.App;
import dev.lzzgabriel.workshop.model.entities.Department;
import dev.lzzgabriel.workshop.model.services.DepartmentService;
import dev.lzzgabriel.workshop.util.Alerts;
import dev.lzzgabriel.workshop.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
  public void onNewButtonAction(ActionEvent e) {
    createDialogForm("DepartmentForm", Utils.currentStage(e));
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
  
  private void createDialogForm(String name, Stage parentStage) {
    try {
      var loader = new FXMLLoader(App.class.getResource(name + ".fxml"));
      Pane pane = loader.load();
      
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Enter department data");
      dialogStage.setScene(new Scene(pane));
      dialogStage.setResizable(false);
      dialogStage.initOwner(parentStage);
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.showAndWait();
    } catch (IOException e) {
      Alerts.showAlert("IOException", "Error load view", e.getMessage(), AlertType.ERROR);
    }
  }

}
