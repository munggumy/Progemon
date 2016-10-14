package logic.player;

import logic.character.ActiveSkill;
import logic.character.Pokemon;

public class HumanPlayer extends Player {

	public HumanPlayer(String name) {
		super(name);
	}

	@Override
	protected boolean inputMove(Pokemon pokemon) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	protected boolean inputAttackPokemon(Pokemon pokemon) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean inputAttackActiveSkill(Pokemon attackingPokemon) {
		// TODO Auto-generated method stub
		return false;
	}
}
