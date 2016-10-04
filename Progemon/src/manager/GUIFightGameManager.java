package manager;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import graphic.DialogBox;
import graphic.Frame;
import graphic.QueueBox;
import graphic.ScreenComponent;
import logic.character.Pokemon;
import logic.player.Player;
import logic.terrain.FightMap;
import utility.FileUtility;
import utility.InputUtility;
import utility.RandomUtility;

public class GUIFightGameManager {

	private static ArrayList<Player> players;
	/** List of player which are not dead. */
	private static ArrayList<Player> currentPlayers;
	private static FightMap fightMap = null;
	/** Find Current Player from this. */
	private static Pokemon currentPokemon = null;
	private static Player winnerPlayer = null;
	private static boolean endturn = false;
	int tick = 0;

	public GUIFightGameManager(ArrayList<Player> players) {

		GUIFightGameManager.players = new ArrayList<Player>(players);
		currentPlayers = new ArrayList<Player>(players);

		// try {
		// FileUtility.loadPokemons();
		// FileUtility.loadPokedex();
		// FileUtility.loadActiveSkills();
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		try {
			fightMap = new FightMap(FileUtility.loadFightMap());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Load Graphics
		new Frame();

		startFight();
		runFight();
		endFight();

	}

	private void startFight() {
		ScreenComponent.addObject(fightMap);

		spawnPokemons();
		fightMap.sortPokemons();

		ScreenComponent.addObject(new DialogBox());
		DialogBox.sentMessage(
				"Pokemon Trainer Brock wants to fight you! \nPokemon Trainer Brock sent Wartortle and Pidgeotto!");
		ScreenComponent.addObject(new QueueBox());
		Frame.getGraphicComponent().repaint();
	}

	private void runFight() {
		while (true) {
			tick++;

			checkInputs();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (DialogBox.hasSentMessage()) {

				if (tick >= 10) {
					if (!endturn) {
						currentPokemon = fightMap.getPokemonsOnMap().get(0);
						currentPokemon.getOwner().runTurn(currentPokemon);
					} else {
						currentPokemon.calculateNextTurnTime();
						currentPokemon.calculateCurrentStats();
						endturn = false;
					}
					removeDeadPokemons();
					fightMap.sortPokemons();
					tick = 0;
				}

			}

			Frame.getGraphicComponent().repaint();

			if (checkWinner()) {
				DialogBox.sentMessage("END OF FIGHT");
				if (DialogBox.hasSentMessage()) {
					System.out.println("The fight has ended.");
					System.out.println("The winner is " + winnerPlayer.getName());
					break;
				}
			}
		}

		System.out.println("END OF FIGHT");
	}

	private void checkInputs() {
		for (InputEvent inputEvent : InputUtility.getInputEvents()) {
			if (inputEvent instanceof MouseEvent) {
				MouseEvent mEvent = (MouseEvent) inputEvent;
				if (mEvent.getID() == MouseEvent.MOUSE_MOVED) {
					InputUtility.setLastMouseMoveEvent(mEvent);
				} else if (mEvent.getID() == MouseEvent.MOUSE_CLICKED) {
					if (mEvent.getButton() == MouseEvent.BUTTON1) {
						InputUtility.setLastMouseClickEvent(mEvent);
					}
				} else if (inputEvent instanceof KeyEvent) {
					KeyEvent kEvent = (KeyEvent) inputEvent;
					InputUtility.setLastKeyEvent(kEvent);
					System.out.println("KEY    \t" + kEvent);
				}
			}
		}
	}

	private void endFight() {
		Frame.getGraphicComponent().repaint();
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

	public static void setEndturn(boolean endturn) {
		GUIFightGameManager.endturn = endturn;
	}

}
