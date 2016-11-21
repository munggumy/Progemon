package main;

import java.util.ArrayList;

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

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(ThreadUtility::showError);
		launch(args);
	}

	private static Thread updateUIThread;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Thread.setDefaultUncaughtExceptionHandler(ThreadUtility::showError);
		
		new GameStage();
		FileUtility.loadAllDefaults();

		Pokemon charlizard = Pokedex.getPokemon("Charlizard");
		charlizard.setLevel(38);
		charlizard.calculateCurrentStats();
		charlizard.resetHP();

		Pokemon caterpie = Pokedex.getPokemon("Caterpie");
		caterpie.setLevel(5);
		caterpie.calculateCurrentStats();
		caterpie.resetHP();

		Pokemon wartortle = Pokedex.getPokemon("Wartortle");
		wartortle.setLevel(34);
		wartortle.calculateCurrentStats();
		wartortle.resetHP();
		Pokemon pidgeotto = Pokedex.getPokemon("Pidgeotto");
		pidgeotto.setLevel(30);
		pidgeotto.setMoveRange(8);
		pidgeotto.calculateCurrentStats();
		pidgeotto.resetHP();

		Player p1 = new HPAIPlayer("AI 1", charlizard, Color.RED);
		p1.addPokemon(caterpie);
		Player p2 = new HumanPlayer("AI 2", pidgeotto, Color.BLUE);
		p2.addPokemon(wartortle);

		ArrayList<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);

		new DrawingUtility();

		// @SuppressWarnings("unused")

		/*
		 * Thread t = new Thread(() -> { GUIFightGameManager gui = new
		 * GUIFightGameManager(players); }); t.start();
		 */

		/*
		 * (this.updateUIThread = new Thread(() -> { GUIFightGameManager gui =
		 * new GUIFightGameManager(players); })).start();
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
	}

}
