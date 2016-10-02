package logic.player;

import logic.character.Pokemon;
import logic.filters.AttackFilter;
import logic.filters.MoveFilter;
import logic.terrain.Path;
import utility.RandomUtility;

public class AIPlayer extends Player {

	public AIPlayer(String name, Pokemon starter_pokemon) {
		super(name, starter_pokemon);
	}

	public AIPlayer(String name) {
		super(name);
	}

	@Override
	public void runTurn(Pokemon pokemon) {
		pokemonMove(pokemon);
		pokemonAttack(pokemon);
	}

	@Override
	public void pokemonMove(Pokemon pokemon) {
		int x = pokemon.getCurrentFightTerrain().getX();
		int y = pokemon.getCurrentFightTerrain().getY();
		pokemon.findBlocksAround(pokemon.getMoveRange(), new MoveFilter());
		pokemon.sortPaths();
//		LinkedList<FightTerrain> nextPath = RandomUtility.randomElement(pokemon.getPaths());
		Path nextPath = pokemon.getPaths().get(0);
		pokemon.move(nextPath.getLast().getX(), nextPath.getLast().getY());
		System.out.println("Pokemon " + pokemon.getName() + " moved from (" + x + ", " + y + ") to ("
				+ pokemon.getCurrentFightTerrain().getX() + ", " + pokemon.getCurrentFightTerrain().getY() + ").");
	}

	@Override
	public void pokemonAttack(Pokemon pokemon) {
		// attack
		pokemon.findBlocksAround(pokemon.getAttackRange(), new AttackFilter());
		for (Pokemon other : pokemon.getCurrentFightMap().getPokemonsOnMap()) {

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