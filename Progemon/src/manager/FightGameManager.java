package manager;

import java.io.IOException;
import java.util.ArrayList;

import logic.character.Player;
import logic.character.Pokemon;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import utility.RandomUtility;
import utility.fileUtility;

public class FightGameManager {
	// null

	private static ArrayList<Player> players;
	private static FightMap field = null;

	/** This method is called before fight starts. */
	public static void startFight() {
		try {
			field = new FightMap(fileUtility.loadFightMap());
		} catch (IOException e) {
			e.printStackTrace();
		}

		spawnPokemons();
		field.sortPokemons();
	}

	/** This method is called after fight ends. */
	public static void endFight() {

	}

	/** This method is called every turn */
	public static void runTurn() {

	}

	private static void spawnPokemons() {
		int nextX, nextY;
		for (Player player : players) {
			for (Pokemon pokemon : player.getPokemons()) {
				do {
					nextX = RandomUtility.randomInt(field.getSizeX() - 1);
					nextY = RandomUtility.randomInt(field.getSizeY() - 1);
				} while(!field.addPokemonToMap(nextX, nextY, pokemon));
			}
		}
	}

}
