package logic.character;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Player {
	private String name;
	private ArrayList<Pokemon> pokemons;
	
	public Player(String name){
		this.name = name;
		pokemons = new ArrayList<Pokemon>();
	}
	
	public Player(String name, Pokemon starter_pokemon){
		this(name);
		pokemons.add(starter_pokemon);
	}
	

}
