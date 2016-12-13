package main;

import javafx.application.Application;
import javafx.stage.Stage;
import manager.GlobalManager;
import utility.ThreadUtility;

public class Main3 extends Application {

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(ThreadUtility::showError);
		launch(args);
	}

	@Override
	public void stop() throws Exception {
		System.exit(0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new GlobalManager().run();
	}

}
