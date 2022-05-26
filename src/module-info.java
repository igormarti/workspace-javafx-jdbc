module javafx {
	requires javafx.controls;
	requires javafx.fxml;
	
	exports controller;
	
	opens controller to javafx.graphics, javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}
