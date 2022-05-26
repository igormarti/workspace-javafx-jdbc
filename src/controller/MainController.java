package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	private void onMenuItemSellerAction() {
		System.out.println("Menu seller");
	}
	
	@FXML
	private void onMenuItemDepartmentAction() {
		System.out.println("Menu department");
	}
	
	@FXML
	private void onMenuItemAboutAction() {
		System.out.println("Menu about");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	
		
	}
}
