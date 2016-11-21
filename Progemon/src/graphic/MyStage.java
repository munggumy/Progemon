package graphic;

import java.awt.event.MouseEvent;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.InputUtility;

public class MyStage extends Stage {

	public MyStage() {
		super();
		setTitle("Progemon");
		GameScreen canvas = new GameScreen();
		Pane root = new Pane();
		root.getChildren().add(canvas);
		Scene scene = new Scene(root, GameScreen.WIDTH, GameScreen.HEIGHT, Color.DARKGRAY);
		setScene(scene);
		addListener(scene);
		setX(0);
		setY(0);
		new AnimationTimer() {

			@Override
			public void handle(long now) {
				GameScreen.repaint();
			}
		}.start();
		show();
	}

	public void addListener(Scene scene) {
		scene.setOnMousePressed(mEvent -> {
			if (mEvent.getButton().equals(MouseButton.PRIMARY)) {
				InputUtility.setMouseLeftDown(true);
			} else if (mEvent.getButton().equals(MouseButton.SECONDARY)) {
				InputUtility.setMouseRightDown(true);
			}
		});

		scene.setOnMouseReleased(mEvent -> {
			if (mEvent.getButton().equals(MouseButton.PRIMARY)) {
				InputUtility.setMouseLeftDown(false);
			} else if (mEvent.getButton().equals(MouseButton.SECONDARY)) {
				InputUtility.setMouseRightDown(false);
			}
		});

		scene.setOnMouseEntered(mEvent -> {
			InputUtility.setMouseOnScreen(true);
		});

		scene.setOnMouseExited(mEvent -> {
			InputUtility.setMouseOnScreen(false);
		});

		scene.setOnMouseMoved(mEvent -> {
			InputUtility.setMouseX((int) mEvent.getX());
			InputUtility.setMouseY((int) mEvent.getY());
		});

		scene.setOnKeyPressed(kEvent -> {
			InputUtility.setKeyPressed(kEvent.getCode(), true);
			InputUtility.setKeyTriggered(kEvent.getCode(), true);
		});

		scene.setOnKeyReleased(kEvent -> {
			InputUtility.setKeyPressed(kEvent.getCode(), false);
			InputUtility.setKeyTriggered(kEvent.getCode(), false);
		});
		System.out.println("Stage Finished Adding Listener");
	}

}
