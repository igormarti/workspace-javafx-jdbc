package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBException;
import gui.listeners.DataChangeListerner;
import gui.utils.Alerts;
import gui.utils.Utils;
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
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListerner {

	@FXML
	private Button btnNew;

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tbColumnDepartmentId;

	@FXML
	private TableColumn<Department, String> tbColumnDepartmentName;

	@FXML
	private TableColumn<Department, Department> tbColumnEdit;

	@FXML
	private TableColumn<Department, Department> tbColumnRemove;

	private ObservableList<Department> observableList;

	public DepartmentListController() {
	}

	@FXML
	public void onBtnNewAction(ActionEvent event) {
		Stage paStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", paStage);
	}

	public void updateTableView() {
		List<Department> list = DepartmentService.findAll();
		observableList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(observableList);
		initEditButtons();
		initRemoveButtons();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		updateTableView();
	}

	private void initializeNodes() {
		tbColumnDepartmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tbColumnDepartmentName.setCellValueFactory(new PropertyValueFactory<>("name"));

		Stage stage = (Stage) Main.getSceneMain().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DepartmentFormController controller = loader.getController();
			controller.setEntity(obj);
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tbColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

		tbColumnEdit.setCellFactory(param -> new TableCell<Department, Department>() {

			private final Button button = new Button("Edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}

		});
	}

	private void initRemoveButtons() {
		tbColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		
		tbColumnRemove.setCellFactory(param -> new TableCell<Department, Department>() {
			
			private final Button button = new Button("Remove");
			
			@Override
			protected void updateItem(Department obj, boolean empty) {
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
	
	private void removeEntity(Department obj) {
		
		try {
			Optional<ButtonType> result =  Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
			
			if(result.get() == ButtonType.OK) {
				DepartmentService.remove(obj);
				updateTableView();
			}
			
		}catch (DBException e) {
			Alerts.showAlert("Error", null, "Error to the try delete department.", AlertType.ERROR);
		}
		
	}

}
