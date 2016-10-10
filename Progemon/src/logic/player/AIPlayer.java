package logic.player;

import java.awt.Color;

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
	private int thinkDelay = 20, thinkDelayCounter = 0;
	private int moveDelay = 5, moveDelayCounter = 0;

	// Constructors

	public AIPlayer(String name, Pokemon starter_pokemon, Color color) {
		super(name, starter_pokemon, color);
	}

	public AIPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	public void pokemonMove(Pokemon pokemon) {
		if (!input) {
			if (thinkDelayCounter == thinkDelay) {
				x = pokemon.getCurrentFightTerrain().getX();
				y = pokemon.getCurrentFightTerrain().getY();
				// LinkedList<FightTerrain> nextPath =
				// RandomUtility.randomElement(pokemon.getPaths());
				nextPath = calculateNextPath(pokemon);
				input = true;
				thinkDelayCounter = 0;
				pokemon.getCurrentFightMap().unshadowAllBlocks();
			} else {
				thinkDelayCounter++;
			}
		} else {
			if (moveCounter == nextPath.size()) {
				System.out.println("Pokemon " + pokemon.getName() + " moved from (" + x + ", " + y + ") to ("
						+ pokemon.getCurrentFightTerrain().getX() + ", " + pokemon.getCurrentFightTerrain().getY()
						+ ").");
				moveCounter = 1;
				input = false;
				setMovePhase(true);
			} else if (moveDelay == moveDelayCounter) {
				if (nextPath.get(moveCounter) != pokemon.getCurrentFightTerrain()) {
					pokemon.move(nextPath.get(moveCounter));
				} else {
					moveCounter++;
				}
				moveDelayCounter = 0;
			} else {
				moveDelayCounter++;
			}
		}
	}

	/** This can be overrided by other AIs */
	protected Path calculateNextPath(Pokemon pokemon) {
		return pokemon.getPaths().get(0);
	}

	@Override
	/**
	 * @param attackingPokemon
	 *            The <code>Pokemon</code> that is used to attack other
	 *            <code>Pokemon</code>.
	 */
	public void pokemonAttack(Pokemon attackingPokemon) {
		// attack
		attackingPokemon.findBlocksAround(attackingPokemon.getAttackRange(), new AttackFilter());
		for (Pokemon other : attackingPokemon.getCurrentFightMap().getPokemonsOnMap()) {

			if (other.getOwner() != this
					&& attackingPokemon.getAvaliableFightTerrains().contains(other.getCurrentFightTerrain())) {
				// If other is enemy and in attack range.
				// pokemon.attack(other, selectedSkill);
				attackingPokemon.attack(other, RandomUtility.randomInt(attackingPokemon.getActiveSkills().size() - 1));
				break;
			}
		} // end of attack
		setAttackPhase(true);
	}
}
