package logic.filters;

import logic.character.Pokemon;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;


public abstract class Filter {
	
	/** Check if pokemon is able to do something at fightTerrain. */
	public abstract boolean check(Pokemon pokemon, FightTerrain fightTerrain);
	public abstract boolean check(Pokemon pokemon, FightMap fightMap, FightTerrain fightTerrain);
}
