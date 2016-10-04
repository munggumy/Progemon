package logic.player;

import java.util.ArrayList;

import logic.character.Pokemon;
import logic.filters.MoveFilter;
import manager.GUIFightGameManager;

public abstract class Player {
	private String name;
	private ArrayList<Pokemon> pokemons;
	private boolean movephrase = false, attackphrase = false, initial = false;

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
	
	public void setAttackphrase(boolean attackphrase) {
		this.attackphrase = attackphrase;
	}
	
	public void setMovephrase(boolean movephrase) {
		this.movephrase = movephrase;
	}
	
	public void addPokemon(Pokemon pokemon){
		pokemon.setOwner(this);
		pokemons.add(pokemon);
	}
	
	
	/** Each turn calls this. */
	public final void runTurn(Pokemon pokemon){
		if(!initial){
			pokemon.findBlocksAround(pokemon.getMoveRange(), new MoveFilter());
			pokemon.sortPaths();
			pokemon.shadowBlocks();
			initial = true;
		}
		else if(!movephrase){
			pokemonMove(pokemon);
		}
		else if(!attackphrase){
			pokemonAttack(pokemon);
		}
		else{
			initial = false;
			movephrase = false;
			attackphrase = false;
			GUIFightGameManager.setEndturn(true);
		}
	}
	
	/** Override <code>this</code> in Each Player Type*/
	public abstract void pokemonMove(Pokemon pokemon);
	/** Override <code>this</code> in Each Player Type*/
	public abstract void pokemonAttack(Pokemon pokemon);

	
}
