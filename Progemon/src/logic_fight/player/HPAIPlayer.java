package logic_fight.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import javafx.scene.paint.Color;
import logic_fight.character.pokemon.Pokemon;
import logic_fight.terrain.FightTerrain;
import logic_fight.terrain.Path;

public class HPAIPlayer extends AIPlayer {

	public HPAIPlayer(String name, Pokemon starter_pokemon, Color color) {
		super(name, starter_pokemon, color);
	}

	public HPAIPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	protected Optional<Path> calculateNextPath(Pokemon pokemon) {
		Pokemon lowestHP = null;
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
		Optional<Path> wholePath = pokemon.findPathTo(lowestHP.getCurrentFightTerrain(), 1000);
		if (!wholePath.isPresent()) {
			return super.calculateNextPath(pokemon);
		}
		Path subPath = wholePath.get().subPath(0, Math.min(wholePath.get().size(), pokemon.getMoveRange() + 1));

		// clear overlap issues
		Path out = new Path();
		boolean overlap = false;
		for (FightTerrain fightTerrain : subPath) {
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
		return Optional.of(out);
	}
}
