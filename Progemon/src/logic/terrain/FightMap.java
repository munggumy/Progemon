package logic.terrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import graphic.DrawingUtility;
import graphic.IRenderable;
import logic.character.Pokemon;
import logic.filters.Filter;
import logic.filters.MoveFilter;
import utility.InputUtility;

public class FightMap implements IRenderable {

	public static final int MAX_SIZE_X = 20;
	public static final int MAX_SIZE_Y = 20;
	private int sizeX, sizeY;
	private FightTerrain[][] map;
	private ArrayList<Pokemon> pokemonsOnMap = new ArrayList<Pokemon>();

	public static enum Direction {
		UP(0, -1), LEFT(-1, 0), DOWN(0, 1), RIGHT(1, 0);

		public int x, y;

		Direction(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

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

	/** Sort by Speed. Used in calculation of Turn Time and Queue. */
	public void sortPokemons() {
		Collections.sort(pokemonsOnMap, Pokemon.bySpeed);
	}

	public FightTerrain[][] getMap() {
		return map;
	}

	public void setMap(FightTerrain[][] map) {
		this.map = map;
	}

	public <T extends Number> FightTerrain getFightTerrainAt(T x, T y) {
		int ix = (int) x;
		int iy = (int) y;
		if (outOfMap(ix, iy)) {
			return null;
		}
		return map[iy][ix];
	}

	public FightTerrain getFightTerrainAt(FightTerrain paramFT, Direction d) {
		return getFightTerrainAt(paramFT.getX() + d.x, paramFT.getY() + d.y);
	}

	public void setFightTerrainAt(int x, int y, FightTerrain fightTerrain) {
		map[y][x] = fightTerrain;
	}

	public Pokemon getPokemonAt(int x, int y) {
		for (Pokemon pokemon : pokemonsOnMap) {
			if (pokemon.getCurrentFightTerrain().getX() == x && pokemon.getCurrentFightTerrain().getY() == y) {
				return pokemon;
			}
		}
		return null;
	}

	public Pokemon getPokemonAt(FightTerrain fightTerrain) {
		return getPokemonAt(fightTerrain.getX(), fightTerrain.getY());
	}

	public boolean outOfMap(int x, int y) {
		if (x < 0 || x >= this.sizeX || y < 0 || y >= this.sizeY) {
			return true;
		} else {
			return false;
		}
	}

	public void draw() {
		int x = InputUtility.getMouseX(), y = InputUtility.getMouseY();
		if (0 <= x && x <= 319 && 0 <= y && y <= 239) {
			getFightTerrainAt((int) Math.floor(x / 40), (int) Math.floor(y / 40)).setCursor(true);
		}
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

	/** @return List<Pokemon> Unmodifiable List of Pokemons on Map. */
	public final List<Pokemon> getPokemonsOnMap() {
		return Collections.unmodifiableList(pokemonsOnMap);
	}

	/**
	 * Use this to add Pokemon to map. Returns true if pokemon can be placed and
	 * places the pokemon.
	 */
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

	/**
	 * Use this to remove Pokemon from map. Returns true if pokemon is on map.
	 */
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

	public void unshadowAllBlocks() {
		for (int j = 0; j < sizeY; j++) {
			for (int i = 0; i < sizeX; i++) {
				map[j][i].setShadowed(false);
				map[j][i].setHighlight(false);
			}
		}
	}

}
