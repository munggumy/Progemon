package logic.player;

import java.util.ArrayList;

import logic.character.Pokemon;

public abstract class Player {
	private String name;
	private ArrayList<Pokemon> pokemons;

	// Constructor

	public Player(String name) {
		this.name = name;
		pokemons = new ArrayList<Pokemon>();
	}

	public Player(String name, Pokemon starter_pokemon) {
		this(name);
		starter_pokemon.setOwner(this);
		pokemons.add(starter_pokemon);
	}

	public Player(String name, Pokemon[] pokemon_set) {
		this(name);
		for (Pokemon pokemon : pokemon_set) {
			pokemon.setOwner(this);
			pokemons.add(pokemon);
		}

	}

	// Run turn

	/** Each turn calls this. */
	public final void runTurn(Pokemon pokemon) {

		pokemonMove(pokemon);
		pokemonAttack(pokemon);
	}

	/** Override <code>this</code> in Each Player Type */
	public abstract void pokemonMove(Pokemon pokemon);

	/** Override <code>this</code> in Each Player Type */
	public abstract void pokemonAttack(Pokemon pokemon);

	/** Checks if this player loses (All pokemons are dead) */
	public boolean isLose() {
		for (Pokemon pokemon : pokemons) {
			if (!pokemon.isDead()) {
				return false;
			}
		}
		return true;
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

}
