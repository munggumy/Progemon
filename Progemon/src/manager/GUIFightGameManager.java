package manager;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import graphic.DialogBox;
import graphic.GameScreen;
import graphic.IRenderable;
import graphic.IRenderableHolder;
import graphic.QueueBox;
import logic.character.Pokemon;
import logic.player.Player;
import logic.terrain.FightMap;
import utility.Clock;
import utility.FileUtility;
import utility.GlobalPhase;
import utility.Phase;
import utility.RandomUtility;
import utility.StringUtility;

public class GUIFightGameManager {

	private static ArrayList<Player> players;
	private static ArrayList<Player> currentPlayers;
	private static FightMap fightMap = null;
	private static Pokemon currentPokemon = null;
	private static Player currentPlayer = null;
	private static Player winnerPlayer = null;
	private static Phase currentPhase;

	public GUIFightGameManager(Set<Player> players) {
		
		GlobalPhase.setCurrentPhase(GlobalPhase.FIGHT);

		GUIFightGameManager.players = new ArrayList<Player>(players);
		currentPlayers = new ArrayList<Player>(players);
		currentPhase = Phase.initialPhase;

		players.stream().forEach(player -> {
			System.out.print("[Player " + player.getName() + " :: ");
			System.out.print(player.getPokemons().stream().map(Pokemon::getName).collect(Collectors.joining(", ")));
			System.out.println("]");
		});

		fightMap = new FightMap(FileUtility.loadFightMap());
		// Load Graphics
		new Clock();
		startFight();
		runFight();
		endFight();
	}

	private void startFight() {
		IRenderableHolder.addFightObject(fightMap);
		spawnPokemons();
		fightMap.sortPokemons();

		IRenderableHolder.addFightObject(new DialogBox());
		IRenderableHolder.addFightObject(new QueueBox());
		System.out.println("Added DialogBox and QueueBox");
		DialogBox.sentMessage("Press '" + DialogBox.advancingKey.toString() + "' to start!");

		System.out.println("Game loaded without problems.");
	}

	private void runFight() {
		while (true) {

			currentPokemon = fightMap.getPokemonsOnMap().get(0);
			currentPlayer = currentPokemon.getOwner();
			currentPhase = Phase.initialPhase;
			currentPlayer.runTurn(currentPokemon); // gives control to player
			currentPokemon.calculateNextTurnTime();

			clearDeadPokemons();
			fightMap.sortPokemons();

			QueueBox.sort();

			if (checkWinner()) {
				System.out.println("The fight has ended.");
				System.out.println("The winner is " + winnerPlayer.getName());
				DialogBox.sentMessage("The winner is " + winnerPlayer.getName());
				break;
			}

			for (int i = 1; i <= 30; i++) {
				Clock.tick(); // wait between turns
			}

		}
		System.out.println("END OF FIGHT");
	}

	private void endFight() {

		while (true) {
			if (DialogBox.hasSentMessage()) {
				break;
			}
			QueueBox.sort();
			DialogBox.update();
			Clock.tick();
		}
		GlobalPhase.setCurrentPhase(GlobalPhase.WORLD);
	}

	private static void spawnPokemons() {
		int nextX, nextY;
		for (Player player : players) {
			// System.out.println(player.getName());
			for (Pokemon pokemon : player.getPokemons()) {
				Objects.requireNonNull(pokemon);
				do {
					nextX = RandomUtility.randomInt(fightMap.getSizeX() - 1);
					nextY = RandomUtility.randomInt(fightMap.getSizeY() - 1);
					// System.out.println(pokemon.getName() + " " + nextX + " "
					// + nextY);
				} while (!fightMap.addPokemonToMap(nextX, nextY, pokemon));
			}
		}
		System.out.println("Finish Spawning Pokemons");
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

	private static void clearDeadPokemons() {
		Function<Pokemon, Double> playerFactor = pokemon -> pokemon.getOwner().isGodlike() ? 1 : 1.5;

		for (int i = fightMap.getPokemonsOnMap().size() - 1; i >= 0; i--) {
			Pokemon p = fightMap.getPokemonsOnMap().get(i);
			if (p.isDead()) {
				System.out.println(p.getName() + " is DEAD!");

				double expYield = p.getExpYield() * playerFactor.apply(p) * p.getLevel() / 7;
				p.getKiller().getOwner().getPokemons().stream().filter(pokemon -> !pokemon.isDead())
						.forEach(pokemon -> {
							// System.out.println("[Pokemon=" +
							// pokemon.getName() + ":lastExpReq=" +
							// pokemon.getLastExpRequired() + ", nextExpReq=" +
							// pokemon.getNextExpRequired() + ", currentExp="+
							// pokemon.getCurrentExp() + "]");
							pokemon.addExpAndTryLevelUp(expYield);
						});
				System.out.println(p.getKiller().getOwner().getName() + "'s pokemon gained "
						+ StringUtility.formatDouble(expYield, 2) + " exp");

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
