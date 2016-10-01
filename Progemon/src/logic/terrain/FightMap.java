package logic.terrain;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import graphic.DrawingUtility;
import graphic.IRenderable;
import logic.character.Pokemon;
import logic.filters.Filter;
import logic.filters.MoveFilter;

public class FightMap implements IRenderable {

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
	
	public void setFightTerrainAt(int x, int y, FightTerrain fightTerrain){
		map[y][x] = fightTerrain;
	}

	public Pokemon getPokemonAt(int x, int y) {
		for (Pokemon pokemon : pokemonsOnMap) {
			if (pokemon.getCurrentFightTerrain().getX() ==  x && pokemon.getCurrentFightTerrain().getY() == y) {
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

	public void draw() {
		DrawingUtility.drawFightMap(this);
	}

	@Override
	public void getDepth() {
		// TODO Auto-generated method stub
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
		Filter canBePlacedFilter = new MoveFilter();
		if (!outOfMap(x, y) && pokemon != null && canBePlacedFilter.check(pokemon, this, map[y][x])
				&& this.getPokemonAt(x, y) == null) {
			// Can be Added!
			pokemon.setCurrentFightMap(this);
			pokemon.move(x, y);
			pokemonsOnMap.add(pokemon);
			return true;
		} else {
			return false;
		}
	}

	/** Use this to remove Pokemon from map */
	public boolean removePokemonFromMap(Pokemon pokemon) {
		if (pokemonsOnMap.contains(pokemon)) {
			pokemon.move(null);
			pokemon.setCurrentFightMap(null);
			pokemonsOnMap.remove(pokemon);
			return true;
		} else {
			return false;
		}
	}

}
