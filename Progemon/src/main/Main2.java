package main;

import java.util.HashSet;
import java.util.Set;

import audio.MusicUtility;
import audio.SFXUtility;
import graphic.DrawingUtility;
import graphic.GameStage;
import item.Items;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic_fight.character.pokemon.Pokemon;
import logic_fight.player.HPAIPlayer;
import logic_fight.player.HumanPlayer;
import logic_fight.player.Player;
import logic_world.player.PlayerCharacter;
import logic_world.terrain.WorldMap;
import logic_world.terrain.WorldObject;
import manager.GUIFightGameManager;
import utility.AnimationUtility;
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
			GameStage gs = new GameStage();
			FileUtility.loadActiveSkills();
			FileUtility.loadPokedex();
			FileUtility.loadStrengthWeaknessTable();

			Items.loadItems();

			new DrawingUtility();

			new MusicUtility();

			new SFXUtility(2);

			new AnimationUtility();

			WorldObject.loadObjectFunctions();

			WorldObject.loadWorldObjects();

			WorldObject.loadObjectImages();

			WorldMap.loadTileset();

			System.out.println("s=" + WorldObject.objectImagesSet.entrySet().size());

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

			Pokemon pidgey1 = Pokedex.getPokemon("Pidgey");
			pidgey1.setLevel(20);

			Pokemon pidgey2 = Pokedex.getPokemon("Pidgey");
			pidgey2.setLevel(20);

			Player p1 = new HPAIPlayer("AI 1", charlizard, Color.RED);
			p1.addPokemon(caterpie);
			p1.addPokemon(pidgey2);
			
			Player p2 = PlayerCharacter.instance.getMe();
			p2.addPokemon(blastoise);
			p2.addPokemon(pidgey1);

			Set<Player> players = new HashSet<Player>();
			players.add(p1);
			players.add(p2);

			new DrawingUtility();
			new MusicUtility();
			new SFXUtility(2);

			// @SuppressWarnings("unused")

			/*
			 * Thread t = new Thread(() -> { GUIFightGameManager gui = new
			 * GUIFightGameManager(players); }); t.start();
			 */

			/*
			 * (this.updateUIThread = new Thread(() -> { GUIFightGameManager gui
			 * = new GUIFightGameManager(players); })).start();
			 */
			Task<Void> main = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					GUIFightGameManager g = new GUIFightGameManager(players, false);
					g.startFight();
					return null;
				}

			};
			gs.show();
			main.setOnFailed(e -> {
				main.getException().printStackTrace();
			});
			Thread logic = new Thread(main);
			logic.setName("Logic Thread");
			logic.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
