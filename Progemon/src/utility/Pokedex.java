package utility;

import java.util.ArrayList;
import java.util.Collection;
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
	
	public static final String getPokemonName(int id){
		return pokedex.get(id);
	}
	
	public static final void addPokemonToPokedex(int id, String name){
		pokedex.put(id, name);
	}
	
	public static final void addPokemonToList(Pokemon pokemon){
		allPokemons.add(pokemon);
		Collections.sort(allPokemons, Pokemon.getIDComparator());
	}
	
	public static final ArrayList<Pokemon> getAllPokemons(){
		return allPokemons;
	}
	
	public static final Pokemon getPokemon(int pokemon_id){
		for (Pokemon pokemon : allPokemons) {
			if (pokemon.getID() == pokemon_id){
				return pokemon;
			}
		}
		return null;
	}
	
	public static final Pokemon getPokemon(String pokemon_name){
		for (Map.Entry<Integer, String> pair : pokedex.entrySet()) {
			if(pair.getValue().equals(pokemon_name)){
				return getPokemon(pair.getKey());
			}
		}
		return null;
	}
	
	

}
