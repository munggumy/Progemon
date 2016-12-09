package graphic;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.InputUtility;

public class GameStage extends Stage {

	public GameStage() {
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
		try {
			// TODO Join
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
				InputUtility.setMouseLeftLastDown(true);
			} else if (mEvent.getButton().equals(MouseButton.SECONDARY)) {
				InputUtility.setMouseRightDown(true);
				InputUtility.setMouseRightLastDown(true);
			}
		});

		scene.setOnMouseReleased(mEvent -> {
			if (mEvent.getButton().equals(MouseButton.PRIMARY)) {
				InputUtility.setMouseLeftDown(false);
				InputUtility.setMouseLeftLastDown(false);
			} else if (mEvent.getButton().equals(MouseButton.SECONDARY)) {
				InputUtility.setMouseRightDown(false);
				InputUtility.setMouseRightLastDown(false);
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
			// System.out.println("KEY PRESSED : " +
			// kEvent.getCode().toString());
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
