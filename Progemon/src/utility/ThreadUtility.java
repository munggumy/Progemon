package utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;

public class ThreadUtility {

	public static void showError(Thread t, Throwable e) {
		System.err.println("*** Default exception handler ***");
		e.printStackTrace();
		if (Platform.isFxApplicationThread()) {
			showErrorDialog(e);
		} else {
			System.err.println("An unexpected error occurred in " + t);
		}
	}

	private static void showErrorDialog(Throwable e) {
		StringWriter errorMsg = new StringWriter();
		e.printStackTrace(new PrintWriter(errorMsg));
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(errorMsg.toString());
		alert.showAndWait();
	}
}
