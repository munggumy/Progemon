package logic.player;

import graphic.Frame;
import javafx.scene.paint.Color;
import logic.character.ActiveSkill;
import logic.character.Pokemon;
import logic.filters.AttackFilter;
import logic.filters.MoveFilter;
import logic.terrain.Path;
import utility.Clock;
import utility.RandomUtility;

public class AIPlayer extends Player {

	private int thinkDelay = 30, thinkDelayCounter = 0;

	// Constructors

	public AIPlayer(String name, Pokemon starter_pokemon, Color color) {
		super(name, starter_pokemon, color);
	}

	public AIPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	protected boolean inputMove(Pokemon pokemon) {
		if (thinkDelayCounter == thinkDelay) {
			super.nextPath = calculateNextPath(pokemon);
			thinkDelayCounter = 0; // reset counter
			return true;
		} else {
			thinkDelayCounter++;
			return false;
		}
	}
	
//	@Override
//	protected final boolean move(Pokemon pokemon) {
//		boolean move = false;
//		int x = pokemon.getCurrentFightTerrain().getX();
//		int y = pokemon.getCurrentFightTerrain().getY();
//		if (moveCounter == nextPath.size()) {
//			System.out.println("Pokemon " + pokemon.getName() + " moved from (" + x + ", " + y + ") to ("
//					+ pokemon.getCurrentFightTerrain().getX() + ", " + pokemon.getCurrentFightTerrain().getY()
//					+ ").");
//			moveCounter = 1;
//			moveDelayCounter = 0;
//			return true;
//		} else if (moveDelay == moveDelayCounter) {
//			if (nextPath.get(moveCounter) != pokemon.getCurrentFightTerrain()) {
//				pokemon.move(nextPath.get(moveCounter));
//			} else {
//				moveCounter++;
//			}
//			moveDelayCounter = 0;
//			return false;
//		} else {
//			moveDelayCounter++;
//			return false;
//		}

		

//	}

	/** This can be overrided by other AIs */
	protected Path calculateNextPath(Pokemon pokemon) {
		return pokemon.getPaths().get(0);
	}

	// @Override
	/**
	 * @param attackingPokemon
	 *            The <code>Pokemon</code> that is used to attack other
	 *            <code>Pokemon</code>.
	 */
	// public void pokemonAttack(Pokemon attackingPokemon) {
	// attack
	// attackingPokemon.findBlocksAround(attackingPokemon.getAttackRange(),
	// new AttackFilter());
	// attackingPokemon.sortPaths();
	// attackingPokemon.shadowBlocks();
	// for (Pokemon other :
	// attackingPokemon.getCurrentFightMap().getPokemonsOnMap()) {
	// if (other.getOwner() != this
	// &&
	// attackingPokemon.getAvaliableFightTerrains().contains(other.getCurrentFightTerrain()))
	// {
	// // If other is enemy and in attack range.
	// other.getCurrentFightTerrain().setHighlight(true);
	// }
	// }
	//
	// Pokemon other = inputAttackPokemon(attackingPokemon);
	// attack(attackingPokemon, other);
	// }

	@Override
	protected boolean inputAttackPokemon(Pokemon attackingPokemon) {
		nextAttackedPokemon = null;
		if (thinkDelayCounter == thinkDelay) {
			for (Pokemon other : attackingPokemon.getCurrentFightMap().getPokemonsOnMap()) {
				if (other.getOwner() != this
						&& attackingPokemon.getAvaliableFightTerrains().contains(other.getCurrentFightTerrain())) {
					// If other is enemy and in attack range.
					thinkDelayCounter = 0;
					attackingPokemon.getCurrentFightMap().unshadowAllBlocks();
					nextAttackedPokemon = other;
				}
			}
			thinkDelayCounter = 0;
			attackingPokemon.getCurrentFightMap().unshadowAllBlocks();
			return true;
		} else {
			thinkDelayCounter++;
			return false;
		}

	}

	@Override
	protected boolean inputAttackActiveSkill(Pokemon attackingPokemon) {
		nextAttackSkill = (ActiveSkill) RandomUtility.randomElement(attackingPokemon.getActiveSkills());
		return true;
	}

	

}
