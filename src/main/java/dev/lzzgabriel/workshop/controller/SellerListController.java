package dev.lzzgabriel.workshop.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dev.lzzgabriel.workshop.App;
import dev.lzzgabriel.workshop.db.DbIntegrityException;
import dev.lzzgabriel.workshop.model.entities.Seller;
import dev.lzzgabriel.workshop.model.services.SellerService;
import dev.lzzgabriel.workshop.util.Alerts;
import dev.lzzgabriel.workshop.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SellerListController implements Initializable {

  private SellerService departmentService;

  private ObservableList<Seller> departments;

  @FXML
  private TableView<Seller> tableView;

  @FXML
  private TableColumn<Seller, Integer> idColumn;

  @FXML
  private TableColumn<Seller, String> nameColumn;

  @FXML
  private TableColumn<Seller, Seller> editColumn;

  @FXML
  private TableColumn<Seller, Seller> removeColumn;

  @FXML
  private Button newButton;

  @FXML
  public void onNewButtonAction(ActionEvent e) {
    Seller obj = new Seller();
    createDialogForm(obj, "SellerForm", Utils.currentStage(e));
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

  public void setSellerService(SellerService departmentService) {
    this.departmentService = departmentService;
  }

  public void updateTableView() {
    if (departmentService == null) {
      throw new IllegalStateException("Seller service unavailable: not set");
    }
    var list = departmentService.findAll();

    departments = FXCollections.observableArrayList(list);
    tableView.setItems(departments);
    initEditButtons();
    initRemoveButtons();
  }

  private void createDialogForm(Seller obj, String name, Stage parentStage) {
//    try {
//      var loader = new FXMLLoader(App.class.getResource(name + ".fxml"));
//      Pane pane = loader.load();
//
//      SellerFormController controller = loader.getController();
//      controller.setSeller(obj);
//      controller.setService(departmentService);
//      controller.addDataChangeListener(() -> updateTableView());
//      controller.updateFormData();
//
//      Stage dialogStage = new Stage();
//      dialogStage.setTitle("Enter department data");
//      dialogStage.setScene(new Scene(pane));
//      dialogStage.setResizable(false);
//      dialogStage.initOwner(parentStage);
//      dialogStage.initModality(Modality.WINDOW_MODAL);
//      dialogStage.showAndWait();
//    } catch (IOException e) {
//      Alerts.showAlert("IOException", "Error load view", e.getMessage(), AlertType.ERROR);
//    }
  }

  private void initEditButtons() {
    editColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    editColumn.setCellFactory(param -> new TableCell<Seller, Seller>() {
      private final Button button = new Button("edit");

      @Override
      protected void updateItem(Seller obj, boolean empty) {
        super.updateItem(obj, empty);
        if (obj == null) {
          setGraphic(null);
          return;
        }
        setGraphic(button);
        button.setOnAction(event -> createDialogForm(obj, "SellerForm", Utils.currentStage(event)));
      }
    });
  }

  private void initRemoveButtons() {
    removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    removeColumn.setCellFactory(param -> new TableCell<Seller, Seller>() {
      private final Button button = new Button("remove");

      @Override
      protected void updateItem(Seller obj, boolean empty) {
        super.updateItem(obj, empty);
        if (obj == null) {
          setGraphic(null);
          return;
        }
        setGraphic(button);
        button.setOnAction(event -> removeEntity(obj));
      }
    });
  }

  private void removeEntity(Seller obj) {
    var opt = Alerts.showConfirmation("Confirmation", "Are you sure?");
    
    if (opt.get() == ButtonType.OK) {
      if (departmentService == null) {
        throw new IllegalStateException("SellerService unavailable: not set");
      }
      try {
        departmentService.remove(obj);
        updateTableView();
      } catch (DbIntegrityException e) {
        Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
      }
    }
    
  }

}
