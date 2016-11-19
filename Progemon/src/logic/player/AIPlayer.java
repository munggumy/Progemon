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
	protected boolean inputNextPath(Pokemon pokemon) {
		if (thinkDelayCounter == thinkDelay) {
			super.nextPath = calculateNextPath(pokemon);
			thinkDelayCounter = 0; // reset counter
			return true;
		} else {
			thinkDelayCounter++;
			return false;
		}
	}
	
	/** This can be overrided by other AIs */
	protected Path calculateNextPath(Pokemon pokemon) {
		return pokemon.getPaths().get(0);
	}

	@Override
	protected boolean inputAttackPokemon(Pokemon attackingPokemon) {
		nextAttackedPokemon = null;
		if (thinkDelayCounter == thinkDelay) {
			for (Pokemon other : attackingPokemon.getCurrentFightMap().getPokemonsOnMap()) {
				if (other.getOwner() != this
						&& attackingPokemon.getAvaliableFightTerrains().contains(other.getCurrentFightTerrain())) {
					// If other is enemy and in attack range.
					nextAttackedPokemon = other;
					break;
				}
			}
			thinkDelayCounter = 0;
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
