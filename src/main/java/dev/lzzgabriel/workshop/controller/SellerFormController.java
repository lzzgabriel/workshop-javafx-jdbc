package dev.lzzgabriel.workshop.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import dev.lzzgabriel.workshop.db.DbException;
import dev.lzzgabriel.workshop.listeners.DataChangeListener;
import dev.lzzgabriel.workshop.model.entities.Department;
import dev.lzzgabriel.workshop.model.entities.Seller;
import dev.lzzgabriel.workshop.model.exceptions.ValidationException;
import dev.lzzgabriel.workshop.model.services.DepartmentService;
import dev.lzzgabriel.workshop.model.services.SellerService;
import dev.lzzgabriel.workshop.util.Alerts;
import dev.lzzgabriel.workshop.util.Constraints;
import dev.lzzgabriel.workshop.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class SellerFormController implements Initializable {

  @FXML
  private TextField txtID;

  @FXML
  private TextField txtName;

  @FXML
  private TextField txtEmail;

  @FXML
  private DatePicker dpBirthDate;

  @FXML
  private TextField txtBaseSalary;

  @FXML
  private ComboBox<Department> cboxDepartment;

  @FXML
  private Label lbErrorName;

  @FXML
  private Label lbErrorEmail;

  @FXML
  private Label lbErrorBirthDate;

  @FXML
  private Label lbErrorBaseSalary;

  @FXML
  private Button btnSave;

  @FXML
  private Button btnCancel;

  private Seller entity;

  private SellerService service;

  private DepartmentService departmentService;

  private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

  private ObservableList<Department> obsList;

  @FXML
  public void onBtnSaveAction(ActionEvent event) {
    if (service == null) {
      throw new IllegalStateException("SellerService not available: not set");
    }
    try {
      entity = getFormData();
      service.saveOrUpdate(entity);
      notifyListeners();
      Utils.currentStage(event).close();
    } catch (DbException e) {
      Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
    } catch (ValidationException e) {
      setErrorMessages(e.getErrors());
    }
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
    Constraints.setTextFieldInteger(txtID);
    Constraints.setTextFieldMaxLength(txtName, 70);
    Constraints.setTextFieldMaxLength(txtEmail, 50);
    Constraints.setTextFieldDouble(txtBaseSalary);
    Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
    initializeComboBoxDepartment();
  }

  public void loadAssociatedObjects() {
    if (departmentService == null) {
      throw new IllegalStateException("DepartmentService unavailable: not set");
    }
    var list = departmentService.findAll();

    obsList = FXCollections.observableArrayList(list);
    cboxDepartment.setItems(obsList);
  }

  public void updateFormData() {
    if (entity == null) {
      throw new IllegalStateException("Entity not available: not set");
    }
    txtID.setText(String.valueOf(entity.getId()));
    txtName.setText(entity.getName());
    txtEmail.setText(entity.getEmail());
    Locale.setDefault(Locale.US);
    txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
    dpBirthDate.setValue(entity.getBirthDate());
    if (entity.getDepartment() == null) {
      cboxDepartment.getSelectionModel().selectFirst();
    } else cboxDepartment.setValue(entity.getDepartment());
  }

  public Seller getFormData() {
    var obj = new Seller();

    var validation = new ValidationException("Validation error");

    obj.setId(Utils.tryParseToInt(txtID.getText()));

    if (txtName.getText() == null || txtName.getText().trim().equals("")) {
      validation.addError("name", "Field can't be empty");
    }
    obj.setName(txtName.getText());

    if (!validation.getErrors().isEmpty()) {
      throw validation;
    }

    return obj;
  }

  private void initializeComboBoxDepartment() {
    Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
      @Override
      protected void updateItem(Department item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : item.getName());
      }
    };
    cboxDepartment.setCellFactory(factory);
    cboxDepartment.setButtonCell(factory.call(null));
  }

  private void setErrorMessages(Map<String, String> errors) {
    var fields = errors.keySet();

    if (fields.contains("name")) {
      lbErrorName.setText(errors.get("name"));
    }
  }

  public void addDataChangeListener(DataChangeListener listener) {
    dataChangeListeners.add(listener);
  }

  public void setSeller(Seller entity) {
    this.entity = entity;
  }

  public void setServices(SellerService service, DepartmentService service2) {
    this.service = service;
    this.departmentService = service2;
  }

}
