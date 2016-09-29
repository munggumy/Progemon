package logic.filters;

import logic.character.Pokemon;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;

/** Checks if Terrain is in attackable area of pokemon. */
public class AttackFilter extends Filter {

	public AttackFilter() {

	}

	/** Check if pokemon if able to attack at fightTerrain. */
	@Override
	public boolean check(Pokemon pokemon, FightTerrain fightTerrain) {
		// TODO Auto-generated method stub
		return true;
	}

}
