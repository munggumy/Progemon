package logic_world.terrain;

import logic_fight.character.pokemon.Pokemon;
import utility.Pokedex;
import utility.RandomUtility;
import utility.StringUtility;

public class SpawningPokemonEntry implements Comparable<SpawningPokemonEntry> {
	private String pokemonName;
	private int minLevel, maxLevel;
	private WildRareness rareness;

	public SpawningPokemonEntry(String pokemonName, int minLevel, int maxLevel, WildRareness rareness) {
		super();
		this.pokemonName = StringUtility.toTitleCase(pokemonName);
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.rareness = rareness;
	}

	public final Pokemon getPokemon() {
		System.out.println("pokemonName=" + pokemonName);
		return Pokedex.getPokemon(pokemonName, RandomUtility.randomInt(minLevel, maxLevel));
	}

	public final double getRate() {
		return rareness.getRate();
	}

	@Override
	public int compareTo(SpawningPokemonEntry other) {
		return Double.compare(this.getRate(), other.getRate());
	}

}