package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtnNewAction() {
		System.out.println("new");
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

}
