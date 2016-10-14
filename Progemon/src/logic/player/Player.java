package logic.player;

import java.awt.Color;
import java.util.ArrayList;

import graphic.Frame;
import logic.character.ActiveSkill;
import logic.character.Pokemon;
import logic.filters.AttackFilter;
import logic.filters.MoveFilter;
import logic.terrain.Path;
import manager.GUIFightGameManager;
import utility.Clock;
import utility.Phase;
import utility.exception.UnknownPhaseException;

public abstract class Player {
	private String name;
	private Color color;
	private ArrayList<Pokemon> pokemons;
	
	protected Pokemon nextAttackedPokemon;
	protected ActiveSkill nextAttackSkill;
	
	protected Path nextPath;
	private int moveCounter = 1;
	private int moveDelay = 5, moveDelayCounter = 0;
	
	private int x, y;

	// Constructor

	protected Player(String name) {
		this.name = name;
		color = Color.BLACK; // default
		pokemons = new ArrayList<Pokemon>();
	}

	protected Player(String name, Color color) {
		this.name = name;
		this.color = color;
		pokemons = new ArrayList<Pokemon>();
	}

	protected Player(String name, Pokemon starter_pokemon, Color color) {
		this(name, color);
		starter_pokemon.setOwner(this);
		pokemons.add(starter_pokemon);
	}

	protected Player(String name, Pokemon[] pokemon_set, Color color) {
		this(name, color);
		for (Pokemon pokemon : pokemon_set) {
			pokemon.setOwner(this);
			pokemons.add(pokemon);
		}
	}

	// Run turn

	/** Each turn calls this. */
	public final void runTurn(Pokemon pokemon) {
		boolean phaseIsFinished = false;
		try {
			while (GUIFightGameManager.getCurrentPhase() != Phase.endPhase) {
				switch (GUIFightGameManager.getCurrentPhase()) {
				case initialPhase:
					phaseIsFinished = true;
					break;

				case preMovePhase:
					pokemon.findBlocksAround(pokemon.getMoveRange(), new MoveFilter());
					pokemon.sortPaths();
					pokemon.shadowBlocks();
					x = pokemon.getCurrentFightTerrain().getX();
					y = pokemon.getCurrentFightTerrain().getY();
					phaseIsFinished = true;
					break;
				case inputMovePhase:
					phaseIsFinished = inputMove(pokemon);
					break;
				case movePhase:
					phaseIsFinished = move(pokemon);
					break;
				case postMovePhase:
					pokemon.getCurrentFightMap().unshadowAllBlocks();
					phaseIsFinished = true;
					break;

				case preAttackPhase:
					pokemon.findBlocksAround(pokemon.getAttackRange(), new AttackFilter());
					pokemon.sortPaths();
					pokemon.shadowBlocks();
					pokemon.getCurrentFightMap().getPokemonsOnMap().stream()
							.filter((Pokemon other) -> !pokemon.getOwner().equals(other.getOwner()))
							.filter((Pokemon other) -> pokemon.getAvaliableFightTerrains().contains(other.getCurrentFightTerrain()))
							.forEach((Pokemon other) -> other.getCurrentFightTerrain().setHighlight(true));
					phaseIsFinished = true;
					break;
				case inputAttackPhase:
					phaseIsFinished = inputAttackPokemon(pokemon) && inputAttackActiveSkill(pokemon);
					break;
				case attackPhase:
					phaseIsFinished = attack(pokemon, nextAttackedPokemon, nextAttackSkill);
					break;
				case postAttackPhase:
					pokemon.getCurrentFightMap().unshadowAllBlocks();
					phaseIsFinished = true;
					break;

				case endPhase:
					phaseIsFinished = true;
					break;
				default:
					throw new UnknownPhaseException("Unknown Phase in Player.java[" + this.name + "].");
				}

				Frame.getGraphicComponent().repaint();
				Clock.tick();

				if (phaseIsFinished) {
					GUIFightGameManager.nextPhase();
					phaseIsFinished = false;
				}
			}
		} catch (UnknownPhaseException e) {
			e.printStackTrace();
		} finally {
			nextAttackedPokemon = null;
			nextAttackSkill = null;
		}

		// pokemonMove(pokemon);
		// pokemonAttack(pokemon);
	}

	protected abstract boolean inputMove(Pokemon pokemon);

	protected final boolean move(Pokemon pokemon) {
		if (moveCounter == nextPath.size()) {
			System.out.println("Pokemon " + pokemon.getName() + " moved from (" + x + ", " + y + ") to ("
					+ pokemon.getCurrentFightTerrain().getX() + ", " + pokemon.getCurrentFightTerrain().getY()
					+ ").");
			moveCounter = 1;
			moveDelayCounter = 0;
			return true;
		} else if (moveDelay == moveDelayCounter) {
			if (nextPath.get(moveCounter) != pokemon.getCurrentFightTerrain()) {
				pokemon.move(nextPath.get(moveCounter));
			} else {
				moveCounter++;
			}
			moveDelayCounter = 0;
			return false;
		} else {
			moveDelayCounter++;
			return false;
		}
	}

	protected abstract boolean inputAttackPokemon(Pokemon pokemon);
	
	protected abstract boolean inputAttackActiveSkill(Pokemon attackingPokemon);

	protected final boolean attack(Pokemon attackingPokemon, Pokemon other, ActiveSkill activeSkill) {
		if (other != null && activeSkill != null) {
			attackingPokemon.attack(other, activeSkill);
		}
		return true;
	}
	
	/** Checks if this player loses (All pokemons are dead) */
	public boolean isLose() {
		return pokemons.stream().allMatch(Pokemon::isDead);
	}

	// Getters

	public final String getName() {
		return name;
	}

	public final ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void addPokemon(Pokemon pokemon) {
		pokemon.setOwner(this);
		pokemons.add(pokemon);
	}

	public final Color getColor() {
		return color;
	}

}
