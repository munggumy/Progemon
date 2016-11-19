package graphic;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.InputUtility;

public class MyStage extends Stage {

	public MyStage() {
		super();
		setTitle("Progemon");
		MyCanvas canvas = new MyCanvas();
		Pane root = new Pane();
		root.getChildren().add(canvas);
		Scene scene = new Scene(root, 800, 600, Color.DARKGRAY);
		setScene(scene);
		addListener(scene);
		setX(0);
		setY(0);
		new AnimationTimer() {

			@Override
			public void handle(long now) {
				MyCanvas.repaint();
			}
		}.start();
		show();
	}

	public void addListener(Scene scene) {
		scene.setOnMouseClicked(mEvent -> {
			InputUtility.setLastMouseClickEvent(mEvent);
		});

		scene.setOnMouseMoved(mEvent -> {
			InputUtility.setLastMouseMoveEvent(mEvent);
			InputUtility.setMouseX((int) mEvent.getX());
			InputUtility.setMouseY((int) mEvent.getY());
		});

		scene.setOnKeyPressed(kEvent -> {
			InputUtility.addEvents(kEvent);
			InputUtility.addKeys(kEvent);
			InputUtility.setLastKeyEvent(kEvent);
		});

		scene.setOnKeyReleased(kEvent -> {
			InputUtility.addEvents(kEvent);
			InputUtility.removeHoldingKeys(kEvent);
			InputUtility.setLastKeyEvent(kEvent);

		});
	}

}
