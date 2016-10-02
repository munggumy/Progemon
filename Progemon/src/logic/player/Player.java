package logic.player;

import java.util.ArrayList;

import logic.character.Pokemon;

public abstract class Player {
	private String name;
	private ArrayList<Pokemon> pokemons;

	public boolean isLose() {
		for (Pokemon pokemon : pokemons) {
			if (!pokemon.isDead()) {
				return false;
			}
		}
		return true;
	}

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

	public final String getName() {
		return name;
	}

	public final ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}
	
	public void addPokemon(Pokemon pokemon){
		pokemon.setOwner(this);
		pokemons.add(pokemon);
	}

	public abstract void runTurn(Pokemon pokemon);
	public abstract void pokemonMove(Pokemon pokemon);
	public abstract void pokemonAttack(Pokemon pokemon);

}