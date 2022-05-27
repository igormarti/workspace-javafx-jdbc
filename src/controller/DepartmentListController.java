package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.utils.Alerts;
import gui.utils.Utils;
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
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	@FXML
	private Button btnNew;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tbColumnDepartmentId;
	
	@FXML
	private TableColumn<Department, String> tbColumnDepartmentName;
	
	private ObservableList<Department> observableList;

	public DepartmentListController() {}
	
	@FXML
	public void onBtnNewAction(ActionEvent event) {
		Stage paStage = Utils.currentStage(event);
		createDialogForm("/gui/DepartmentForm.fxml", paStage );
	}
	
	public void updateTableView() {
		List<Department> list = DepartmentService.findAll();
		observableList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(observableList);
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
	
	private void createDialogForm(String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.showAlert("Error", null,e.getMessage(), AlertType.ERROR);
		}
	}

}
