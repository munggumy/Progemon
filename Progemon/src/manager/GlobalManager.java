package manager;

import audio.MusicUtility;
import audio.SFXUtility;
import graphic.DrawingUtility;
import graphic.GameStage;
import item.Bag;
import item.Items;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.concurrent.Worker.State;
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
import logic_fight.character.pokemon.Pokemon;
import logic_world.player.PlayerCharacter;
import logic_world.terrain.WorldMap;
import logic_world.terrain.WorldObject;
import utility.AnimationUtility;
import utility.FileUtility;
import utility.Pokedex;
import utility.ThreadUtility;

public class GlobalManager {
	
	WorldManager worldManager = new WorldManager();

	public void run() {
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

		Pokemon charlizard = Pokedex.getPokemon("Charlizard");
		charlizard.setLevel(40);

		Pokemon caterpie = Pokedex.getPokemon("Caterpie");
		caterpie.setLevel(5);

		PlayerCharacter.instance.addPokemon(charlizard);
		PlayerCharacter.instance.addPokemon(caterpie);

		Bag bag = new Bag();
		bag.addAll(Items.getItem("potion"), Items.getItem("potion"), Items.getItem("potion"), Items.getItem("soda_pop"),
				Items.getItem("pokeball"), Items.getItem("pokeball"), Items.getItem("pokeball"),
				Items.getItem("pokeball"), Items.getItem("great_ball"));

		PlayerCharacter.instance.setBag(bag);

		Task<Void> main = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				worldManager.start();
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
