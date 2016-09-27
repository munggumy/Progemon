package utility;

import java.util.HashMap;

public class Pokedex {
	private static HashMap<Integer, String> pokedex = new HashMap<Integer, String>();

	public static final HashMap<Integer, String> getPokedex() {
		return pokedex;
	}
	
	public static final String getPokemonName(int id){
		return pokedex.get(id);
	}
	
	public static final void addPokemon(int id, String name){
		pokedex.put(id, name);
	}

}
