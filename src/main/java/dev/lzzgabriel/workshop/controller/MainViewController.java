package dev.lzzgabriel.workshop.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import dev.lzzgabriel.workshop.App;
import dev.lzzgabriel.workshop.model.services.DepartmentService;
import dev.lzzgabriel.workshop.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

  @FXML
  private MenuItem menuItemSeller;
  @FXML
  private MenuItem menuItemDepartment;
  @FXML
  private MenuItem menuItemAbout;
  
  @FXML
  public void onMenuItemSellerAction() {
    System.out.println("onMenuItemSellerAction");
  }
  
  @FXML
  public void onMenuItemDepartmentAction() {
    loadView("DepartmentList", (DepartmentListController controller) -> {
      controller.setDepartmentService(new DepartmentService());
      controller.updateTableView();
    });
  }
  
  @FXML
  public void onMenuItemAboutAction() {
    loadView("About", noop -> {});
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
  
  private synchronized <T> void loadView(String name, Consumer<T> initializingAction) {
    try {
      var loader = new FXMLLoader(App.class.getResource(name + ".fxml"));
      var newVbox = (VBox) loader.load();
      
      var scene = App.getScene();
      var mainVbox = (VBox) ((ScrollPane) scene.getRoot()).getContent();
      
      var menu = mainVbox.getChildren().get(0);
      mainVbox.getChildren().clear();
      mainVbox.getChildren().add(menu);
      mainVbox.getChildren().addAll(newVbox.getChildren());
      
      initializingAction.accept(loader.getController());
    } catch (IOException e) {
      Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), null);
    }
  }
  
}
