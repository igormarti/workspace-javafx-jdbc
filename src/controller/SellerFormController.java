package controller;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import exception.ValidationException;
import gui.listeners.DataChangeListerner;
import gui.utils.Alerts;
import gui.utils.Constraints;
import gui.utils.Utils;
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
import model.entities.Department;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;
	
	private List<DataChangeListerner> dataChangeListerners = new ArrayList<DataChangeListerner>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private ComboBox<Department> cbDepartment;
	
	@FXML
	private Label lblErrorName;
	
	@FXML
	private Label lblErrorEmail;
	
	@FXML
	private Label lblErrorBirthDate;
	
	@FXML
	private Label lblErrorBaseSalary;
	
	@FXML
	private Label lblErrorDepartment;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;
	
	private ObservableList<Department> obList;

	@FXML
	private void onBtnSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity is null.");
		}
		try {
			Seller seller = getFormData();
			SellerService.saveOrUpdate(seller);
			notifyDataChangeListener();
			Utils.currentStage(event).close();
		}catch (ValidationException exception) {
			setErrorMessage(exception.getErrors());
		} 
		catch (DBException e) {
			Alerts.showAlert("Error", null, "Error to the try save department", AlertType.ERROR);
		}
	}

	private void notifyDataChangeListener() {
	
		for(DataChangeListerner listener: dataChangeListerners) {
			listener.onDataChanged();
		}
		
	}

	private Seller getFormData() {
		
		ValidationException exception = new ValidationException();
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty");
		}
		
		if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty");
		}
		
		if(dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		Seller obj = new Seller();
		obj.setId( Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		obj.setEmail(txtEmail.getText());
		
		Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setBirthDate(Date.from(instant));
		
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		obj.setDepartament(cbDepartment.getValue());
		
		return obj;
	}

	@FXML
	private void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setEntity(Seller entity) {
		this.entity = entity;
	}
	
	public void subscribeDataChangeListener(DataChangeListerner listerner) {
		dataChangeListerners.add(listerner);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(txtName, 100);
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 100);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity is null.");
		}

		Locale.setDefault(Locale.US);
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if(entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		
		if(entity.getDepartament() == null) {
			cbDepartment.getSelectionModel().selectFirst();
		} else {
			cbDepartment.setValue(entity.getDepartament());
		}
		
	}
	
	public void loadAssociatedObjects() {
		List<Department> list = DepartmentService.findAll();
		obList = FXCollections.observableArrayList(list);
		cbDepartment.setItems(obList);
	}
	
	private void initializeComboBoxDepartment() {
		
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		
		cbDepartment.setCellFactory(factory);
		cbDepartment.setButtonCell(factory.call(null));
	}
	
	public void setErrorMessage(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			lblErrorName.setText(errors.get("name"));
		}
		
		if(fields.contains("email")) {
			lblErrorEmail.setText(errors.get("email"));
		}
		
		if(fields.contains("baseSalary")) {
			lblErrorBaseSalary.setText(errors.get("baseSalary"));
		}
		
		if(fields.contains("birthDate")) {
			lblErrorBirthDate.setText(errors.get("birthDate"));
		}
	}

}
