package logic.filters;

import logic.character.Pokemon;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;

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
