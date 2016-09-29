package logic.character;

import java.util.ArrayList;

import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import utility.RandomUtility;

public class AIPlayer extends Player {

	public AIPlayer(String name, Pokemon starter_pokemon) {
		super(name, starter_pokemon);
	}

	public AIPlayer(String name) {
		super(name);
	}

	@Override
	public void runTurn(Pokemon pokemon, FightMap fightMap) {
		int x = pokemon.getX();
		int y = pokemon.getY();
		ArrayList<FightTerrain> movableBlocks = fightMap.findMovableBlockAround(pokemon.getMoveRange(),
				fightMap.getFightTerrainAt(x, y), new ArrayList<FightTerrain>(), pokemon);
		FightTerrain nextBlock = RandomUtility.randomElement(movableBlocks);
		pokemon.move(nextBlock.getX(), nextBlock.getY());
		System.out.println("Pokemon " + pokemon.getName() + " moved from (" + x + ", " + y + ") to (" + pokemon.getX()
				+ ", " + pokemon.getY() + ").");
	}

}
