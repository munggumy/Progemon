package logic.player;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.paint.Color;
import logic.character.Pokemon;
import logic.terrain.FightTerrain;
import logic.terrain.Path;

public class HPAIPlayer extends AIPlayer {

	public HPAIPlayer(String name, Pokemon starter_pokemon, Color color) {
		super(name, starter_pokemon, color);
	}

	public HPAIPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	protected Path calculateNextPath(Pokemon pokemon) {
		Pokemon lowestHP = null;
		// find pokemon with lowest HP
		ArrayList<Pokemon> sortedList = new ArrayList<Pokemon>(pokemon.getCurrentFightMap().getPokemonsOnMap());
		Collections.sort(sortedList, Pokemon.byHP);
		for (Pokemon other : sortedList) {
			if (other != null && other.getOwner() != pokemon.getOwner()) {
				lowestHP = other;
				break;
			}
		}
		if (lowestHP == null) {
			return super.calculateNextPath(pokemon);
		}
		Path wholePath = pokemon.findPathTo(lowestHP.getCurrentFightTerrain(), 1000);
		if (wholePath == null) {
			return super.calculateNextPath(pokemon);
		}
		Path subPath = wholePath.subPath(0, Math.min(wholePath.size(), pokemon.getMoveRange() + 1));

		// clear overlap issues
		Path out = new Path();
		boolean overlap = false;
		for (FightTerrain fightTerrain : subPath) {
			// for (Pokemon otherPokemon :
			// pokemon.getCurrentFightMap().getPokemonsOnMap()) {
			// if (otherPokemon != pokemon &&
			// otherPokemon.getCurrentFightTerrain().equals(fightTerrain)) {
			// overlap = true;
			// }
			// }
			overlap = pokemon.getCurrentFightMap().getPokemonsOnMap().stream().anyMatch(other -> {
				return other != pokemon && other.getCurrentFightTerrain().equals(fightTerrain);
			});
			if (overlap) {
				break;
			} else {
				out.add(fightTerrain);
			}
		}
		if (out.size() == 0) {
			// in case this is not moving anywhere
			return super.calculateNextPath(pokemon);
		}
		return out;
	}
}
