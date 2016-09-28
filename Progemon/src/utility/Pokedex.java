package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import logic.character.Pokemon;

public class Pokedex {
	private static HashMap<Integer, String> pokedex = new HashMap<Integer, String>();
	private static ArrayList<Pokemon> allPokemons = new ArrayList<Pokemon>();

	public static final HashMap<Integer, String> getPokedex() {
		return pokedex;
	}

	/** Returns null if pokemon not in Pokedex. */
	public static final String getPokemonName(int id) {
		if (pokedex.containsKey(id)) {
			return pokedex.get(id);
		} else {
			return null;
		}
	}

	/** Returns -1 if pokemon not in Pokedex. */
	public static final int getPokemonID(String name) {
		for (Map.Entry<Integer, String> entry : pokedex.entrySet()) {
			if (entry.getValue().equals(name)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	public static final void addPokemonToPokedex(int id, String name) {
		pokedex.put(id, name);
	}

	public static final void addPokemonToList(Pokemon pokemon) {
		allPokemons.add(pokemon);
		Collections.sort(allPokemons, Pokemon.getIDComparator());
	}

	public static final ArrayList<Pokemon> getAllPokemons() {
		return allPokemons;
	}

	public static final Pokemon getPokemon(int pokemon_id) {
		for (Pokemon pokemon : allPokemons) {
			if (pokemon.getID() == pokemon_id) {
				return pokemon;
			}
		}
		return null;
	}

	public static final Pokemon getPokemon(String pokemon_name) {
		return getPokemon(getPokemonID(pokemon_name));
	}

}
