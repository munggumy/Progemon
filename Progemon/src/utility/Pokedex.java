package utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.character.pokemon.PokemonTemplate;

public class Pokedex {
	// private static HashMap<Integer, String> pokedex = new HashMap<Integer,
	// String>();
	// private static ArrayList<Pokemon> allPokemons = new ArrayList<Pokemon>();
	private static Map<String, PokemonTemplate> pokedex = new HashMap<String, PokemonTemplate>();

	public static final Map<String, PokemonTemplate> getPokedex() {
		return pokedex;
	}

	/**  */
	public static final String getPokemonName(int id) {
		List<PokemonTemplate> out = pokedex.values().stream().filter(pt -> pt.getId() == id)
				.collect(Collectors.toList());
		if (out.size() > 1) {
			throw new PokedexException("getPokemonName() : found more than one name for id=" + id);
		} else if (out.size() == 0) {
			throw new PokedexException("getPokemonName() : cannot find pokemon name for id=" + id);
		}
		return out.get(0).getName();
	}

	/** Returns -1 if pokemon not in Pokedex. */
	public static final int getPokemonID(String name) {
		try {
			return pokedex.get(name).getId();
		} catch (NullPointerException ex) {
			throw new PokedexException("getPokemonID() : cannot find pokemon name=" + name, ex);
		}
	}

	public static final void addPokemonToPokedex(String name, PokemonTemplate pokemonTemplate) {
		pokedex.put(StringUtility.toTitleCase(name), pokemonTemplate);
	}

	// get Pokemon methods

	/** main <code>getPokemon()</code> method. gets a clone */
	public static final Pokemon getPokemon(String name, int level) {
		PokemonTemplate pt = pokedex.get(name);
		if (pt == null) {
			throw new PokedexException("getPokemon() : cannot get Pokemon name=" + name);
		}
		return new Pokemon(pt, level);
	}

	public static final Pokemon getPokemon(String name) {
		return getPokemon(name, Pokemon.DEFAULT_POKEMON_LEVEL);
	}

	public static final void clearPokedex() {
		pokedex.clear();
	}

}
