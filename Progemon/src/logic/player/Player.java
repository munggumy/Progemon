package logic.player;

import java.awt.Color;
import java.util.ArrayList;

import logic.character.Pokemon;
import logic.filters.MoveFilter;
import manager.GUIFightGameManager;

public abstract class Player {
	private String name;
	private Color color;
	private ArrayList<Pokemon> pokemons;
	private boolean movePhase = false, attackPhase = false, initial = false;

	// Constructor

	public Player(String name) {
		this.name = name;
		pokemons = new ArrayList<Pokemon>();
	}

	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
		pokemons = new ArrayList<Pokemon>();
	}

	public Player(String name, Pokemon starter_pokemon, Color color) {
		this(name);
		starter_pokemon.setOwner(this);
		this.color = color;
		pokemons.add(starter_pokemon);
	}

	public Player(String name, Pokemon[] pokemon_set, Color color) {
		this(name);
		for (Pokemon pokemon : pokemon_set) {
			pokemon.setOwner(this);
			pokemons.add(pokemon);
		}
		this.color = color;
	}

	// Run turn

	/** Each turn calls this. */
	public final void runTurn(Pokemon pokemon) {
		if (!initial) {
			pokemon.findBlocksAround(pokemon.getMoveRange(), new MoveFilter());
			pokemon.sortPaths();
			pokemon.shadowBlocks();
			initial = true;
		} else if (!movePhase) {
			pokemonMove(pokemon);
		} else if (!attackPhase) {
			pokemonAttack(pokemon);
		} else {
			initial = false;
			movePhase = false;
			attackPhase = false;
			GUIFightGameManager.setEndturn(true);
		}
	}

	/** Override <code>this</code> in Each Player Type */
	public abstract void pokemonMove(Pokemon pokemon);

	/** Override <code>this</code> in Each Player Type */
	public abstract void pokemonAttack(Pokemon pokemon);

	/** Checks if this player loses (All pokemons are dead) */
	public boolean isLose() {
		return pokemons.stream().filter((Pokemon pokemon) -> !pokemon.isDead()).count() >= 1;
//		for (Pokemon pokemon : pokemons) {
//			if (!pokemon.isDead()) {
//				return false;
//			}
//		}
//		return true;
	}

	// Getters

	public final String getName() {
		return name;
	}

	public final ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setAttackPhase(boolean attackphrase) {
		this.attackPhase = attackphrase;
	}

	public void setMovePhase(boolean movephrase) {
		this.movePhase = movephrase;
	}

	public void addPokemon(Pokemon pokemon) {
		pokemon.setOwner(this);
		pokemons.add(pokemon);
	}

	public final Color getColor() {
		return color;
	}
	
	

}
