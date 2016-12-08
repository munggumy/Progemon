package logic_fight.filters;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.terrain.FightMap;
import logic_fight.terrain.FightTerrain;

public class OverlapFilter extends Filter{

	@Override
	public boolean check(Pokemon pokemon, FightTerrain fightTerrain) {
		for(Pokemon other : pokemon.getCurrentFightMap().getPokemonsOnMap()){
			if (other == pokemon){
				continue;
			}
			if(other.getCurrentFightTerrain().equals(fightTerrain)){
				return false;
			}
		}
		return true;
	}
	
	/** Pokemon doesn't know it's map yet. */
	@Override
	public boolean check(Pokemon pokemon, FightMap fightMap, FightTerrain fightTerrain){
		for(Pokemon other : fightMap.getPokemonsOnMap()){
			if (other == pokemon){
				continue;
			}
			if(other.getCurrentFightTerrain().equals(fightTerrain)){
				return false;
			}
		}
		return true;
	}

}
