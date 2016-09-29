package logic.terrain;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logic.character.Pokemon;
import logic.filters.Filter;
import logic.filters.MoveFilter;

public class FightMap {

	public static final int MAX_SIZE_X = 20;
	public static final int MAX_SIZE_Y = 20;
	private int sizeX, sizeY;
	private FightTerrain[][] map;
	private ArrayList<Pokemon> pokemonsOnMap = new ArrayList<Pokemon>();

	public FightMap(int sizeX, int sizeY) {
		if (sizeX < 0) {
			sizeX = 0;
		} else if (sizeX > MAX_SIZE_X) {
			sizeX = MAX_SIZE_X;
		}
		if (sizeY < 0) {
			sizeY = 0;
		} else if (sizeY > MAX_SIZE_Y) {
			sizeY = MAX_SIZE_Y;
		}
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		map = new FightTerrain[sizeY][sizeX];
	}

	public FightMap(FightTerrain[][] ft) {
		map = ft;
		sizeX = map[0].length;
		sizeY = map.length;
	}

	public void sortPokemons() {
		Collections.sort(pokemonsOnMap, Pokemon.getSpeedComparator());
	}

	public FightTerrain[][] getMap() {
		return map;
	}

	public void setMap(FightTerrain[][] map) {
		this.map = map;
	}

	public FightTerrain getFightTerrainAt(int x, int y) {
		return map[y][x];
	}

	public Pokemon getPokemonAt(int x, int y) {
		for (Pokemon pokemon : pokemonsOnMap) {
			if (pokemon.getX() == x && pokemon.getY() == y) {
				return pokemon;
			}
		}
		return null;
	}

	public boolean outOfMap(int x, int y) {
		if (x < 0 || x >= this.sizeX || y < 0 || y >= this.sizeY) {
			return true;
		} else {
			return false;
		}
	}

	public static void draw(Graphics2D g2) {
		System.out.println("reach");
		g2.fillRect(100, 100, 100, 100);
		/*
		 * for (FightTerrain[] fightTerrains : map) { for (FightTerrain
		 * fightTerrain : fightTerrains) { fightTerrain.draw(g2); } }
		 */
	}

	public final int getSizeX() {
		return sizeX;
	}

	public final int getSizeY() {
		return sizeY;
	}

	public final List<Pokemon> getPokemonsOnMap() {
		return Collections.unmodifiableList(pokemonsOnMap);
	}

	/** Use this to add Pokemon to map. */
	public boolean addPokemonToMap(int x, int y, Pokemon pokemon) {
		Filter canBePlacedFitler = new MoveFilter();
		if (!outOfMap(x, y) && pokemon != null && canBePlacedFitler.check(pokemon, map[y][x])
				&& this.getPokemonAt(x, y) == null) {
			// Can be Added!
			pokemon.setX(x);
			pokemon.setY(y);
			pokemon.setCurrentFightMap(this);
			pokemonsOnMap.add(pokemon);
			return true;
		} else {
			return false;
		}
	}

	/** Use this to remove Pokemon from map */
	public boolean removePokemonFromMap(Pokemon pokemon) {
		if (pokemonsOnMap.contains(pokemon)) {
			pokemon.setX(new Integer(null));
			pokemon.setY(new Integer(null));
			pokemon.setCurrentFightMap(null);
			pokemonsOnMap.remove(pokemon);
			return true;
		} else {
			return false;
		}
	}

}
