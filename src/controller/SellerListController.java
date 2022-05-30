package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListerner {

	@FXML
	private Button btnNew;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tbColumnSellerId;

	@FXML
	private TableColumn<Seller, String> tbColumnSellerName;
	
	@FXML
	private TableColumn<Seller, String> tbColumnSellerEmail;
	
	@FXML
	private TableColumn<Seller, Date> tbColumnSellerBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tbColumnSellerBaseSalary;

	@FXML
	private TableColumn<Seller, Seller> tbColumnEdit;

	@FXML
	private TableColumn<Seller, Seller> tbColumnRemove;

	private ObservableList<Seller> observableList;

	public SellerListController() {
	}

	@FXML
	public void onBtnNewAction(ActionEvent event) {
		Stage paStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", paStage);
	}

	public void updateTableView() {
		List<Seller> list = SellerService.findAll();
		observableList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(observableList);
		initEditButtons();
		initRemoveButtons();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		updateTableView();
	}

	private void initializeNodes() {
		tbColumnSellerId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tbColumnSellerName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tbColumnSellerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tbColumnSellerBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tbColumnSellerBirthDate,"dd/MM/yyyy");
		tbColumnSellerBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tbColumnSellerBaseSalary, 2);
		
		Stage stage = (Stage) Main.getSceneMain().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController();
			controller.setEntity(obj);
			controller.loadAssociatedObjects();
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

		tbColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {

			private final Button button = new Button("Edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
							event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event))
						);
			}

		});
	}

	private void initRemoveButtons() {
		tbColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		
		tbColumnRemove.setCellFactory(param -> new TableCell<Seller, Seller>() {
			
			private final Button button = new Button("Remove");
			
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
		
		try {
			Optional<ButtonType> result =  Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
			
			if(result.get() == ButtonType.OK) {
				SellerService.remove(obj);
				updateTableView();
			}
			
		}catch (DBException e) {
			Alerts.showAlert("Error", null, "Error to the try delete department.", AlertType.ERROR);
		}
		
	}

}
