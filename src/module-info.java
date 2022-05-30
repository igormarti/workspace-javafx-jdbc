module javafx {
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	
	exports controller;
	exports model.entities;
	exports model.services;
	exports gui.listeners;
	
	opens controller to javafx.graphics, javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}
