package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.player.Player;
import utility.exception.DuplicatePokemonException;
import utility.exception.UnknownPokemonException;

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
			if (entry.getValue().equalsIgnoreCase(name)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	public static final void addPokemonToPokedex(int id, String name) {
		if (!pokedex.containsKey(id) && !pokedex.containsValue(name)) {
			// pokemon not in dictionary
			pokedex.put(id, name);
		}
	}

	public static final void addPokemonToList(Pokemon pokemon) {
		for (Pokemon p : allPokemons) {
			if (p.getID() == pokemon.getID()) {
				throw new DuplicatePokemonException("Pokedex : Duplicate Pokemon : " + pokemon.getName());
			}
		}
		allPokemons.add(pokemon);
		Collections.sort(allPokemons, Pokemon.byID);
	}

	public static final ArrayList<Pokemon> getAllPokemons() {
		return allPokemons;
	}

	// get Pokemon methods

	/** main <code>getPokemon()</code> method. gets a clone */
	public static final Pokemon getPokemon(int pokemon_id) {
		for (Pokemon pokemon : allPokemons) {
			if (pokemon.getID() == pokemon_id) {
				try {
					Pokemon temp = (Pokemon) pokemon.clone();
					return temp;
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
		List<Pokemon> results = allPokemons.stream().filter(pokemon -> (pokemon.getID() == pokemon_id))
				.collect(Collectors.toList());
		if (results.size() == 1) {
			return results.get(0);
		}
		if (results.size() > 1) {
			throw new DuplicatePokemonException("More than one pokemon found for id = " + pokemon_id);
		} else if (results.isEmpty()) {
			throw new UnknownPokemonException("Unknown Pokemon id =  " + pokemon_id);
		}
		return null;
	}

	public static final Pokemon getPokemon(String pokemon_name) {
		return getPokemon(getPokemonID(pokemon_name));
	}

	public static final Pokemon getPokemon(int pokemon_id, Player owner) {
		Pokemon out = getPokemon(pokemon_id);
		out.setOwner(owner);
		return out;
	}

	public static final Pokemon getPokemon(String pokemon_name, Player owner) {
		Pokemon out = getPokemon(pokemon_name);
		out.setOwner(owner);
		return out;
	}

	public static final void printPokedex() {
		for (Map.Entry<Integer, String> entry : pokedex.entrySet()) {
			System.out.println("ID " + entry.getKey() + " = " + entry.getValue());
		}
	}

	public static final void clearPokedex() {
		pokedex.clear();
	}

	public static final void clearAllPokemons() {
		allPokemons.clear();
	}

}
