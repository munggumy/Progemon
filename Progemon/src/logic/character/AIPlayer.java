package logic.character;

import logic.filters.AttackFilter;
import logic.filters.MoveFilter;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import utility.RandomUtility;
import utility.Savable;

public class AIPlayer extends Player implements Savable {

	public AIPlayer(String name, Pokemon starter_pokemon) {
		super(name, starter_pokemon);
	}

	public AIPlayer(String name) {
		super(name);
	}

	@Override
	public void runTurn(Pokemon pokemon, FightMap fightMap) {
		int x = pokemon.getX();
		int y = pokemon.getY();

		// move
		pokemon.findBlocksAround(pokemon.getMoveRange(), new MoveFilter());
		FightTerrain nextBlock = RandomUtility.randomElement(pokemon.getBlocks());
		pokemon.move(nextBlock.getX(), nextBlock.getY());
		System.out.println("Pokemon " + pokemon.getName() + " moved from (" + x + ", " + y + ") to (" + pokemon.getX()
				+ ", " + pokemon.getY() + ").");

		// attack
		pokemon.findBlocksAround(pokemon.getAttackRange(), new AttackFilter());
		for (Pokemon other : fightMap.getPokemonsOnMap()) {
			
			if (!other.getOwner().equals(this) && pokemon.getBlocks().contains(other.getCurrentFightTerrain())) {
				// If other is enemy and in attack range.
				// pokemon.attack(other, selectedSkill);
				pokemon.attack(other, 1);
				
			}
			
		}
	}

}
