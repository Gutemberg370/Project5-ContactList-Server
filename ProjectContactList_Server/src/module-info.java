module ProjectContactList_Server {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.rmi;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application to java.rmi;
}
