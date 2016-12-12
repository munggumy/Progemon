package main;

import audio.MusicUtility;
import audio.SFXUtility;
import graphic.DrawingUtility;
import graphic.GameStage;
import item.Items;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import logic_world.terrain.WorldMap;
import logic_world.terrain.WorldObject;
import manager.WorldManager;
import utility.AnimationUtility;
import utility.FileUtility;
import utility.ThreadUtility;

public class Main3 extends Application {

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(ThreadUtility::showError);
		launch(args);
	}

	// private static Thread updateUIThread;

	@Override
	public void stop() throws Exception {
		System.exit(0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Thread.setDefaultUncaughtExceptionHandler(ThreadUtility::showError);
		final Group root = new Group();
		Pane waitingPane = new StackPane();
		waitingPane.setPrefSize(400, 300);
		waitingPane.setBackground(
				new Background(new BackgroundFill(Color.DODGERBLUE, new CornerRadii(1), new Insets(10))));

		final ProgressBar progress = new ProgressBar(0);
		progress.setTranslateY(-25);
		progress.setMinWidth(200);
		final Label load = new Label("loading...");
		load.setFont(Font.font("Helvetica Neue", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 25));
		load.setTextFill(Color.ALICEBLUE);
		load.setTranslateY(25);
		waitingPane.getChildren().addAll(progress, load);

		root.getChildren().add(waitingPane);
		Stage s = new Stage();
		s.setScene(new Scene(root, 400, 300));
		s.setX(0);
		s.setY(0);

		GameStage gs = new GameStage();

		Task<Void> preLoad = new Task<Void>() {
			@Override
			protected Void call() throws Exception {

				int num = 12;
				FileUtility.loadActiveSkills();
				progress.setProgress(1.00 / num);
				FileUtility.loadPokedex();
				progress.setProgress(2.00 / num);
				FileUtility.loadStrengthWeaknessTable();
				progress.setProgress(3.00 / num);
				Items.loadItems();
				progress.setProgress(4.00 / num);
				new DrawingUtility();
				progress.setProgress(5.00 / num);
				new MusicUtility();
				progress.setProgress(6.00 / num);
				new SFXUtility(2);
				progress.setProgress(7.00 / num);
				new AnimationUtility();
				progress.setProgress(8.00 / num);
				WorldObject.loadObjectFunctions();
				progress.setProgress(9.00 / num);
				WorldObject.loadWorldObjects();
				progress.setProgress(10.00 / num);
				WorldObject.loadObjectImages();
				progress.setProgress(11.00 / num);
				WorldMap.loadTileset();
				progress.setProgress(12.00 / num);
				System.out.println("s=" + WorldObject.objectImagesSet.entrySet().size());

				return null;
			}
		};

		preLoad.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				load.setText("Failed to load...");
				preLoad.getException().printStackTrace();
				System.err.println("Stopped Loading at progress = " + progress.getProgress());
			}

		});

		preLoad.stateProperty().addListener(new ChangeListener<Worker.State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldState, State newState) {
				if (newState == Worker.State.SUCCEEDED) {
					s.close();
				}
			}

		});

		new Thread(preLoad).start();
		s.showAndWait();

		// Pokemon charlizard = Pokedex.getPokemon("Charlizard");
		// charlizard.addActiveSkill(ActiveSkill.getActiveSkill("Flamethrower"));
		// charlizard.setLevel(38);
		//
		// Pokemon caterpie = Pokedex.getPokemon("Caterpie");
		// charlizard.addActiveSkill(ActiveSkill.getActiveSkill("Tackle"));
		// caterpie.setLevel(5);
		//
		// Pokemon wartortle = Pokedex.getPokemon("Wartortle");
		// charlizard.addActiveSkill(ActiveSkill.getActiveSkill("Tackle"));
		// wartortle.setLevel(34);
		//
		// Pokemon pidgeotto = Pokedex.getPokemon("Pidgeotto");
		// pidgeotto.setLevel(30);
		// pidgeotto.setMoveRange(8);
		//
		// Player p1 = new HPAIPlayer("AI 1", charlizard, Color.RED);
		// p1.addPokemon(caterpie);
		// Player p2 = new AIPlayer("AI 2", pidgeotto, Color.BLUE);
		// p2.addPokemon(wartortle);
		//
		// ArrayList<Player> players = new ArrayList<Player>();
		// players.add(p1);
		// players.add(p2);

		// @SuppressWarnings("unused")

		/*
		 * Thread t = new Thread(() -> { GUIFightGameManager gui = new
		 * GUIFightGameManager(players); }); t.start();
		 */

		/*
		 * (this.updateUIThread = new Thread(() -> { GUIFightGameManager gui =
		 * new GUIFightGameManager(players); })).start();
		 */

		Task<Void> main = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				new WorldManager();
				return null;
			}

		};

		main.setOnFailed(e -> {
			main.getException().printStackTrace();
		});

		Thread logic = new Thread(main);
		logic.setName("Logic Thread");
		logic.start();
		gs.show();
	}

}
