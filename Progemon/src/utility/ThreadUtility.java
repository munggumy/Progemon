package utility;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(getCause(e).getMessage());
		alert.setContentText(errorMsg.toString());
		alert.showAndWait();
	}

	public static Throwable getCause(Throwable e) {
		Throwable cause = null;
		Throwable result = e;

		while (null != (cause = result.getCause()) && (result != cause)) {
			result = cause;
		}
		return result;
	}
}
