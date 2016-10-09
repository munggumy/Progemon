package logic.filters;

import logic.character.Pokemon;
import logic.character.Pokemon.MoveType;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;

public class MoveNoOverlapFilter extends Filter {

	@Override
	public boolean check(Pokemon pokemon, FightTerrain fightTerrain) {
		return checkTerrain(pokemon, fightTerrain);
	}

	@Override
	public boolean check(Pokemon pokemon, FightMap fightMap, FightTerrain fightTerrain) {
		return checkTerrain(pokemon, fightTerrain);
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
