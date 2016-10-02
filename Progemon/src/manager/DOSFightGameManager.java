package manager;

import java.io.IOException;
import java.util.ArrayList;

import logic.character.Pokemon;
import logic.player.Player;
import logic.terrain.FightMap;
import utility.FileUtility;
import utility.RandomUtility;

public class DOSFightGameManager {
	// null

	private static ArrayList<Player> players;
	private static ArrayList<Player> currentPlayers;
	private static FightMap field = null;
	private static Pokemon currentPokemon = null;
	private static Player winnerPlayer = null;

	public DOSFightGameManager(ArrayList<Player> players) {
		DOSFightGameManager.players = new ArrayList<Player>(players);
		DOSFightGameManager.currentPlayers = new ArrayList<Player>(players);
		try {
			field = new FightMap(FileUtility.loadFightMap());
		} catch (IOException e) {
			e.printStackTrace();
		}

		startFight();
		runFight();
		endFight();
	}

	/** This method is called before fight starts. */
	public static void startFight(/** map name */
	) {

		spawnPokemons();
		field.sortPokemons();
		System.out.println(" ======= Game Initialized without errors ======= ");
	}

	/** This method is called after fight ends. */
	public static void endFight() {

	}

	/** This method is called to run. */
	public static void runFight() {
		int i = 1;
		while (true) {
			currentPokemon = field.getPokemonsOnMap().get(0);
			currentPokemon.getOwner().runTurn(currentPokemon);

			removeDeadPokemons();

			if (checkWinner()) {
				System.out.println("The fight has ended.");
				System.out.println("The winner is " + winnerPlayer.getName());
				break;
			}

			System.out.println(currentPokemon.getName() + " : " + currentPokemon.getAttackStat() + " "
					+ currentPokemon.getCurrentHP() + " " + currentPokemon.getFullHP());
			currentPokemon.calculateNextTurnTime();
			currentPokemon.calculateCurrentStats();

			field.sortPokemons();
			i++;
		}

		System.out.println("END at round i = " + i);

	}

	private static void spawnPokemons() {
		int nextX, nextY;
		for (Player player : players) {
			for (Pokemon pokemon : player.getPokemons()) {
				do {
					nextX = RandomUtility.randomInt(field.getSizeX() - 1);
					nextY = RandomUtility.randomInt(field.getSizeY() - 1);
				} while (pokemon != null && !field.addPokemonToMap(nextX, nextY, pokemon));
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

	public static void removeDeadPokemons() {
		for (int i = field.getPokemonsOnMap().size() - 1; i >= 0; i--) {
			Pokemon p = field.getPokemonsOnMap().get(i);
			if (p.isDead()) {
				System.out.println(p.getName() + " is DEAD!");
				field.removePokemonFromMap(p);
			}
		}
	}

	public static final ArrayList<Player> getPlayers() {
		return players;
	}

	public static final ArrayList<Player> getCurrentPlayers() {
		return currentPlayers;
	}

	public static final FightMap getField() {
		return field;
	}

	public static final Pokemon getCurrentPokemon() {
		return currentPokemon;
	}

	public static final Player getWinner() {
		return winnerPlayer;
	}

}
