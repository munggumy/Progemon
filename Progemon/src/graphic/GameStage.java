package graphic;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.InputUtility;

public class GameStage extends Stage {

	private static GameScreen canvas;
	private static FullScreen fullCanvas;
	private static Scene normalScene;
	private static Scene fullScene;

	public GameStage() {
		super();
		setTitle("Progemon");
		canvas = new GameScreen();
		fullCanvas = new FullScreen();
		Pane root = new Pane();
		root.getChildren().add(canvas);
		root.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));

		Pane root2 = new Pane();
		root2.getChildren().add(fullCanvas);
		root2.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));

		normalScene = new Scene(root, GameScreen.WIDTH, GameScreen.HEIGHT, Color.BLACK);
		fullScene = new Scene(root2, FullScreen.WIDTH, FullScreen.HEIGHT, Color.BLACK);
		fullScene.setFill(Color.BLACK);

		setScene(normalScene);
		addListener(normalScene);
		addListener(fullScene);
		addFullScreenListener(fullScene);
		setX(0);
		setY(0);
		setResizable(false);
		setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.F12));
		setFullScreenExitHint("Press F12 to exit full-screen mode.");
		sizeToScene();
		new AnimationTimer() {

			@Override
			public void handle(long now) {
				try {
					GameScreen.repaint();
				} catch (Exception e) {
				}
				if (getScene() == fullScene) {
					FullScreen.repaint();
				}
			}
		}.start();
	}

	public void addListener(Scene scene) {
		scene.setOnMousePressed(mEvent -> {
			if (mEvent.getButton().equals(MouseButton.PRIMARY)) {
				InputUtility.setMouseLeftClick(true);
			} else if (mEvent.getButton().equals(MouseButton.SECONDARY)) {
				InputUtility.setMouseRightClick(true);
			}
		});

		scene.setOnMouseReleased(mEvent -> {
			if (mEvent.getButton().equals(MouseButton.PRIMARY)) {
				InputUtility.setMouseLeftPress(false);
			} else if (mEvent.getButton().equals(MouseButton.SECONDARY)) {
				InputUtility.setMouseRightPress(false);
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

		scene.setOnMouseDragged(mEvent -> {
			InputUtility.setMouseX((int) mEvent.getX());
			InputUtility.setMouseY((int) mEvent.getY());
		});

		scene.setOnScroll(sEvent -> {
			InputUtility.setScroll((int) (sEvent.getDeltaY() / 40));
		});

		scene.setOnKeyPressed(kEvent -> {
			// System.out.println("KEY PRESSED : " +
			// kEvent.getCode().toString());
			InputUtility.setKeyTriggered(kEvent.getCode(), true);
			InputUtility.setKeyPressed(kEvent.getCode(), true);
			if (kEvent.getCode() == KeyCode.F12) {
				if (getScene().equals(normalScene)) {
					setScene(fullScene);
					setFullScreen(true);
				} else {
					setScene(normalScene);
				}
			}
		});

		scene.setOnKeyReleased(kEvent -> {
			InputUtility.setKeyPressed(kEvent.getCode(), false);
			InputUtility.setKeyTriggered(kEvent.getCode(), false);
		});

		System.out.println("Scene " + scene + " Finished Adding Listener");
	}

	public void addFullScreenListener(Scene scene) {
		scene.setOnMouseMoved(mEvent -> {
			int x = (int) ((mEvent.getX() - FullScreen.X_ORIGIN) / FullScreen.RESIZE_RATE);
			int y = (int) (mEvent.getY() / FullScreen.RESIZE_RATE);
			InputUtility.setMouseX(x);
			InputUtility.setMouseY(y);
		});

		scene.setOnMouseDragged(mEvent -> {
			int x = (int) ((mEvent.getX() - FullScreen.X_ORIGIN) / FullScreen.RESIZE_RATE);
			int y = (int) (mEvent.getY() / FullScreen.RESIZE_RATE);
			InputUtility.setMouseX(x);
			InputUtility.setMouseY(y);
		});
	}

	public static GameScreen getCanvas() {
		return canvas;
	}

}
