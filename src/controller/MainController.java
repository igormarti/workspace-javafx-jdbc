package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	private void onMenuItemSellerAction () {
		loadView("/gui/SellerList.fxml", (SellerListController c) -> c.updateTableView());
	}
	
	@FXML
	private void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController c) -> c.updateTableView());
	}
	
	@FXML
	private void onMenuItemAboutAction() {
		loadView("/gui/AboutView.fxml", x -> {});
	}
	
	public synchronized <T> void loadView(String absoluteName, Consumer<T> initializeAction) {
		
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
	    	
			VBox newVBox = loader.load();
			Scene mainScene = Main.getSceneMain();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			initializeAction.accept(loader.getController());
			
		} catch (IOException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	
		
	}
}
