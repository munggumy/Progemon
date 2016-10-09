package logic.player;

import java.awt.Color;

import logic.character.Pokemon;

public class HumanPlayer extends Player {

	public HumanPlayer(String name, Pokemon starter_pokemon, Color color) {
		super(name, starter_pokemon, color);
	}
	
	public HumanPlayer(String name, Color color){
		super(name, color);
	}

	/*@Override
	public void runTurn(Pokemon pokemon) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void pokemonMove(Pokemon pokemon) {
		
	}

	@Override
	public void pokemonAttack(Pokemon pokemon) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
