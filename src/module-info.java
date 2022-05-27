module javafx {
	requires javafx.controls;
	requires javafx.fxml;
	
	exports controller;
	exports model.entities;
	exports model.services;
	
	opens controller to javafx.graphics, javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}
