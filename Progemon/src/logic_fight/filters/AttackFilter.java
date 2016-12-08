
package logic_fight.filters;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.terrain.FightMap;
import logic_fight.terrain.FightTerrain;

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
	
	@Override
	public boolean check(Pokemon pokemon, FightMap fightMap, FightTerrain fightTerrain) {
		// TODO Auto-generated method stub
		return true;
	}
	

}

