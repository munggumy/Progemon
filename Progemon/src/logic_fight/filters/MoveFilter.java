package logic_fight.filters;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.character.pokemon.Pokemon.MoveType;
import logic_fight.terrain.FightMap;
import logic_fight.terrain.FightTerrain;

public class MoveFilter extends OverlapFilter {
	
	@Override
	public boolean check(Pokemon pokemon, FightTerrain fightTerrain) {
		return checkTerrain(pokemon, fightTerrain) && super.check(pokemon, fightTerrain);
	}
	
	@Override
	public boolean check(Pokemon pokemon, FightMap fightMap, FightTerrain fightTerrain){
		return checkTerrain(pokemon, fightTerrain) && super.check(pokemon, fightMap, fightTerrain);
	}

	public boolean checkTerrain(Pokemon pokemon, FightTerrain fightTerrain) {
		MoveType mt = pokemon.getMoveType();
		switch (fightTerrain.getType()) {
		case WATER:
			return mt.equals(MoveType.SWIM) || mt.equals(MoveType.FLY);
		case ROCK:
		case TREE:
			return mt.equals(MoveType.FLY);
		case GRASS:
		case GROUND:
		default:
			return true;
		}
	}	

}