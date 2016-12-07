package main;

import java.util.HashSet;
import java.util.Set;

import graphic.DrawingUtility;
import graphic.GameStage;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.character.Pokemon;
import logic.player.HPAIPlayer;
import logic.player.HumanPlayer;
import logic.player.Player;
import manager.GUIFightGameManager;
import utility.FileUtility;
import utility.Pokedex;
import utility.ThreadUtility;

public class Main2 extends Application {

	static {
		Thread.setDefaultUncaughtExceptionHandler(ThreadUtility::showError);
	}

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(ThreadUtility::showError);
		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Thread updateUIThread;

	@Override
	public void stop() throws Exception {
		super.stop();
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Thread.setDefaultUncaughtExceptionHandler(ThreadUtility::showError);
			new GameStage();
			FileUtility.loadAllDefaults();

			Pokemon charlizard = Pokedex.getPokemon("Charlizard");
			charlizard.setLevel(40);

			Pokemon caterpie = Pokedex.getPokemon("Caterpie");
			caterpie.setLevel(5);
			caterpie.showStats();

			Pokemon blastoise = Pokedex.getPokemon("Blastoise");
			blastoise.setLevel(50);

			Pokemon pidgeotto = Pokedex.getPokemon("Pidgeotto");
			pidgeotto.setLevel(30);
			pidgeotto.setMoveRange(8);

			Player p1 = new HPAIPlayer("AI 1", charlizard, Color.RED);
			p1.addPokemon(caterpie);
			Player p2 = new HumanPlayer("Kris", pidgeotto, Color.BLUE);
			p2.addPokemon(blastoise);

			Set<Player> players = new HashSet<Player>();
			players.add(p1);
			players.add(p2);

			new DrawingUtility();

			// @SuppressWarnings("unused")

			/*
			 * Thread t = new Thread(() -> { GUIFightGameManager gui = new
			 * GUIFightGameManager(players); }); t.start();
			 */

			/*
			 * (this.updateUIThread = new Thread(() -> { GUIFightGameManager gui
			 * = new GUIFightGameManager(players); })).start();
			 */
			Thread logic = new Thread(new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					new GUIFightGameManager(players);
					return null;
				}

			});
			logic.setName("Logic Thread");
			logic.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
