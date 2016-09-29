package logic.filters;

import logic.character.Pokemon;
import logic.character.Pokemon.MoveType;
import logic.terrain.FightTerrain;

public class MoveFilter extends Filter {

	@Override
	public boolean check(Pokemon pokemon, FightTerrain fightTerrain) {
		MoveType mt = pokemon.getMoveType();
		switch (fightTerrain.getType()){
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
