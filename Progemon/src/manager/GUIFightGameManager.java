package manager;

import java.io.IOException;
import java.util.ArrayList;

import graphic.Frame;
import graphic.ScreenComponent;
import logic.character.Player;
import logic.character.Pokemon;
import logic.terrain.FightMap;
import utility.FileUtility;
import utility.RandomUtility;

public class GUIFightGameManager {

	private static ArrayList<Player> players;
	private static ArrayList<Player> currentPlayers;
	private static FightMap fightMap = null;
	private static Pokemon currentPokemon = null;
	private static Player winnerPlayer = null;
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		new Frame();

		startFight();
		runFight();
		endFight();

	}

	private void startFight() {
		ScreenComponent.addObject(fightMap);
		spawnPokemons();
		fightMap.sortPokemons();
		for (Pokemon pokemon : fightMap.getPokemonsOnMap()) {
			ScreenComponent.addObject(pokemon);
		}
		Frame.getGraphicComponent().repaint();
	}

	private void runFight() {
		while (true) {
			tick++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (tick == 100) {
				currentPokemon = fightMap.getPokemonsOnMap().get(0);
				currentPokemon.getOwner().runTurn(currentPokemon);
				currentPokemon.calculateNextTurnTime();
				currentPokemon.calculateCurrentStats();
				tick = 0;
				removeDeadPokemons();
			}
			Frame.getGraphicComponent().repaint();

			if (checkWinner()) {
				System.out.println("The fight has ended.");
				System.out.println("The winner is " + winnerPlayer.getName());
				break;
			}


			fightMap.sortPokemons();
		}

		System.out.println("END OF FIGHT");
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
				ScreenComponent.getObjectOnScreen().remove(p);
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

}
