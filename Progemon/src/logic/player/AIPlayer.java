package logic.player;

import logic.character.Pokemon;
import logic.filters.AttackFilter;
import logic.filters.MoveFilter;
import logic.terrain.Path;
import utility.RandomUtility;

public class AIPlayer extends Player {
	
	private boolean input = false;
	private Path nextPath;
	private int x, y;
	private int moveCounter = 1;
	private int thinkDelay = 10, thinkDelayCounter = 0;

	public AIPlayer(String name, Pokemon starter_pokemon) {
		super(name, starter_pokemon);
	}

	public AIPlayer(String name) {
		super(name);
	}

	/*@Override
	public void runTurn(Pokemon pokemon) {
		pokemonMove(pokemon);
		pokemonAttack(pokemon);
	}*/

	@Override
	public void pokemonMove(Pokemon pokemon) {
		if(!input){
			if(thinkDelayCounter == thinkDelay){
				x = pokemon.getCurrentFightTerrain().getX();
				y = pokemon.getCurrentFightTerrain().getY();
	//			LinkedList<FightTerrain> nextPath = RandomUtility.randomElement(pokemon.getPaths());
				nextPath = pokemon.getPaths().get(0);
				input = true;
				thinkDelayCounter = 0;
				pokemon.getCurrentFightMap().unshadowAllBlocks();
			}
			else{
				thinkDelayCounter++;
			}
		}
		else{
			if(moveCounter < nextPath.size()){
				if(nextPath.get(moveCounter) != pokemon.getCurrentFightTerrain()){
					pokemon.move(nextPath.get(moveCounter));
				}
				else{
					moveCounter++;
				}
			}
			else{
				System.out.println("Pokemon " + pokemon.getName() + " moved from (" + x + ", " + y + ") to ("
						+ pokemon.getCurrentFightTerrain().getX() + ", " + pokemon.getCurrentFightTerrain().getY() + ").");
				moveCounter = 1;
				input = false;
				setMovephrase(true);
			}
		}
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
		setAttackphrase(true);
	}
}
