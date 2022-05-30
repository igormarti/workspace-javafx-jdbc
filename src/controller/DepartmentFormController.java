package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import exception.ValidationException;
import gui.listeners.DataChangeListerner;
import gui.utils.Alerts;
import gui.utils.Constraints;
import gui.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;
	
	private List<DataChangeListerner> dataChangeListerners = new ArrayList<DataChangeListerner>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label lblErrorName;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	@FXML
	private void onBtnSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity is null.");
		}
		try {
			Department department = getFormData();
			DepartmentService.saveOrUpdate(department);
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

	private Department getFormData() {
		
		ValidationException exception = new ValidationException();
		
		Integer Id = Utils.tryParseToInt(txtId.getText());
		String Name = txtName.getText();
		
		if(Name == null || Name.trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return new Department(Id, Name);
	}

	@FXML
	private void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setEntity(Department entity) {
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
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldInteger(txtId);
	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity is null.");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	
	public void setErrorMessage(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			lblErrorName.setText(errors.get("name"));
		}
		
	}

}
