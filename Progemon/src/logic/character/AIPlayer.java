package logic.character;

import logic.filters.AttackFilter;
import logic.filters.MoveFilter;
import logic.terrain.FightMap;
import logic.terrain.PathNode;
import utility.RandomUtility;
import utility.Savable;

public class AIPlayer extends Player {

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
		PathNode nextPath = RandomUtility.randomElement(pokemon.getPaths());
		pokemon.move(nextPath.getThisNode().getX(), nextPath.getThisNode().getY());
		System.out.println("Pokemon " + pokemon.getName() + " moved from (" + x + ", " + y + ") to (" + pokemon.getX()
				+ ", " + pokemon.getY() + ").");

		// attack
		pokemon.findBlocksAround(pokemon.getAttackRange(), new AttackFilter());
		for (Pokemon other : fightMap.getPokemonsOnMap()) {

			if (other.getOwner() != this
					&& pokemon.getAvaliableFightTerrains().contains(other.getCurrentFightTerrain())) {
				// If other is enemy and in attack range.
				// pokemon.attack(other, selectedSkill);
				pokemon.attack(other, RandomUtility.randomInt(pokemon.getActiveSkills().size() - 1));
				break;
			}
		} // end of attack
	}
}
