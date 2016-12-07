
package logic_fight.filters;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.terrain.FightMap;
import logic_fight.terrain.FightTerrain;


public abstract class Filter {
	
	/** Check if pokemon is able to do something at fightTerrain. */
	public abstract boolean check(Pokemon pokemon, FightTerrain fightTerrain);
	public abstract boolean check(Pokemon pokemon, FightMap fightMap, FightTerrain fightTerrain);
}

