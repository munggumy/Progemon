package manager;

import java.io.IOException;
import java.util.ArrayList;

import logic.character.Player;
import logic.character.Pokemon;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import utility.RandomUtility;
import utility.FileUtility;

public class FightGameManager {
	// null

	private static ArrayList<Player> players;
	private static ArrayList<Player> currentPlayers;
	private static FightMap field = null;
	private static Pokemon currentPokemon = null;
	private static Player winner = null;
	
	public FightGameManager(ArrayList<Player> players){
		FightGameManager.players = players;
		FightGameManager.currentPlayers = players;
		
		startFight();
		runFight();
		endFight();
	}

	/** This method is called before fight starts. */
	public static void startFight(/** map name */) {
		try {
			field = new FightMap(FileUtility.loadFightMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FLAG 01");
		spawnPokemons();
		System.out.println("FLAG 02");
		field.sortPokemons();
		System.out.println("FLAG 03");
	}

	/** This method is called after fight ends. */
	public static void endFight() {

	}

	/** This method is called to run.*/
	public static void runFight() {
		int i = 3;
		while (i >= 0) {
			currentPokemon = field.getPokemonsOnMap().get(0);
			currentPokemon.getOwner().runTurn(currentPokemon, field);
			
			if(checkWinner()){
				break;
			}
			
			currentPokemon.calculateNextTurnTime();
			
			field.sortPokemons();
			i--;
		}
		
		System.out.println("i = " + i);
		
	}

	private static void spawnPokemons() {
		int nextX, nextY;
		for (Player player : players) {
			for (Pokemon pokemon : player.getPokemons()) {
				do {
					nextX = RandomUtility.randomInt(field.getSizeX() - 1);
					nextY = RandomUtility.randomInt(field.getSizeY() - 1);
				} while(pokemon != null && !field.addPokemonToMap(nextX, nextY, pokemon));
			}
		}
	}
	
	private static boolean checkWinner(){
		for (int i = currentPlayers.size() - 1; i>= 0 ; i--) {
			if(!currentPlayers.get(i).isLose()){
				currentPlayers.remove(i);
			}
		}
		
		if(currentPlayers.size() == 1){
			winner = currentPlayers.get(0);
		}
		if (currentPlayers.size() == 0){
			//Draws
			winner = null;
		}
		return winner != null;
		
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
		return winner;
	}

}
