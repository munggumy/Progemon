package manager;

<<<<<<< HEAD
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
=======
import java.io.IOException;
>>>>>>> 568bada80f9cfbe97bd4e374df501a6a2b400cf2
import java.util.ArrayList;
import java.util.List;

import graphic.DialogBox;
import graphic.Frame;
import graphic.MyCanvas;
import graphic.MyStage;
import graphic.QueueBox;
import graphic.ScreenComponent;
import javafx.event.Event;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import logic.character.Pokemon;
import logic.player.HumanPlayer;
import logic.player.Player;
import logic.terrain.FightMap;
import utility.Clock;
import utility.FileUtility;
import utility.InputUtility;
import utility.Phase;
import utility.RandomUtility;

public class GUIFightGameManager {

	private static ArrayList<Player> players;
	private static ArrayList<Player> currentPlayers;
	private static FightMap fightMap = null;
	private static Pokemon currentPokemon = null;
	private static Player currentPlayer = null;
	private static Player winnerPlayer = null;
	private static Phase currentPhase = null;

	public GUIFightGameManager(ArrayList<Player> players) {

		GUIFightGameManager.players = new ArrayList<Player>(players);
		currentPlayers = new ArrayList<Player>(players);
		currentPhase = Phase.initialPhase;

		fightMap = new FightMap(FileUtility.loadFightMap());

		// Load Graphics
		new Clock();
		startFight();
		runFight();
		endFight();
	}

	private void startFight() {

		MyCanvas.addObject(fightMap);

		spawnPokemons();
		fightMap.sortPokemons();

		MyCanvas.addObject(new DialogBox());
		MyCanvas.addObject(new QueueBox());

		DialogBox.sentMessage("Press 'a' to start!");

		System.out.println("Game loaded without problems.");
	}

	private void runFight() {
		while (true) {

			currentPokemon = fightMap.getPokemonsOnMap().get(0);
			currentPlayer = currentPokemon.getOwner();
			currentPhase = Phase.initialPhase;
			currentPlayer.runTurn(currentPokemon); // gives control to player
			currentPokemon.calculateNextTurnTime();
			currentPokemon.calculateCurrentStats();

			removeDeadPokemons();
			fightMap.sortPokemons();

			QueueBox.sort();

			if (checkWinner()) {
				System.out.println("The fight has ended.");
				System.out.println("The winner is " + winnerPlayer.getName());
				DialogBox.sentMessage("END OF FIGHT");
				break;
			}

			for (int i = 1; i <= 30; i++) {
				Clock.tick(); // wait between turns
			}
		}
		System.out.println("END OF FIGHT");
	}

	public static void checkInputs() {
		for (Event event : InputUtility.getEvents()) {
			if (event instanceof MouseEvent) {
				MouseEvent mEvent = (MouseEvent) event;
				if (mEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
					InputUtility.setLastMouseMoveEvent(mEvent);
					System.out.println("MOVE   \t" + mEvent);
				} else if (mEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mEvent.getButton() == MouseButton.PRIMARY) {
					InputUtility.setLastMouseClickEvent(mEvent);
					System.out.println("CLICKED\t" + mEvent);
				} // end mouse event
			} else if (event instanceof KeyEvent) {
				KeyEvent kEvent = (KeyEvent) event;
				InputUtility.setLastKeyEvent(kEvent);
				System.out.println("KEY    \t" + kEvent);
				if (kEvent.getText().equals(" ")) {
					if (kEvent.getEventType() == KeyEvent.KEY_PRESSED) {
						Clock.setTps(300);
					} else if (kEvent.getEventType() == KeyEvent.KEY_RELEASED) {
						Clock.setTps(60);
					}
				} // end key event
			}
		}
	}

	private void endFight() {

		while (true) {

			checkInputs();

			if (DialogBox.hasSentMessage()) {
				break;
			}

			QueueBox.sort();
			DialogBox.update();

			Clock.tick();
		}

	}

	private static void spawnPokemons() {
		int nextX, nextY;
		for (Player player : players) {
			for (Pokemon pokemon : player.getPokemons()) {
				do {
					nextX = RandomUtility.randomInt(fightMap.getSizeX() - 1);
					nextY = RandomUtility.randomInt(fightMap.getSizeY() - 1);
				} while (pokemon != null && !fightMap.addPokemonToMap(nextX, nextY, pokemon));
			}
		}
	}

	private static boolean checkWinner() {
		for (int i = currentPlayers.size() - 1; i >= 0; i--) {
			if (currentPlayers.get(i).isLose()) {
				currentPlayers.remove(i);
			}
		}

		if (currentPlayers.size() == 1) {
			winnerPlayer = currentPlayers.get(0);
		}
		if (currentPlayers.size() == 0) {
			// Draws
			winnerPlayer = null;
		}
		return winnerPlayer != null;

	}

	private static void removeDeadPokemons() {
		for (int i = fightMap.getPokemonsOnMap().size() - 1; i >= 0; i--) {
			Pokemon p = fightMap.getPokemonsOnMap().get(i);
			if (p.isDead()) {
				System.out.println(p.getName() + " is DEAD!");
				fightMap.removePokemonFromMap(p);
			}
		}
	}

	public static final ArrayList<Player> getPlayers() {
		return players;
	}

	public static final ArrayList<Player> getCurrentPlayers() {
		return currentPlayers;
	}

	public static final FightMap getFightMap() {
		return fightMap;
	}

	public static final Pokemon getCurrentPokemon() {
		return currentPokemon;
	}

	public static final Player getWinnerPlayer() {
		return winnerPlayer;
	}

	public static final Phase getCurrentPhase() {
		return currentPhase;
	}

	public static final void nextPhase() {
		currentPhase = currentPhase.nextPhase();
	}

}
