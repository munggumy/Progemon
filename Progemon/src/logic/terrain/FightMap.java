package logic.terrain;

import java.util.ArrayList;
import java.util.Collections;

import logic.character.Pokemon;
import utility.RandomUtility;

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
	
	public void sortPokemons(){
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

	public ArrayList<FightTerrain> findMovableBlockAround(int range, FightTerrain ft, ArrayList<FightTerrain> fts,
			Pokemon pokemon) {
		if (range >= 0 && pokemon.getMoveType().check(ft)) {
			int x = ft.getX();
			int y = ft.getY();
			fts.add(map[x][y]);
			if (x < this.sizeX) {
				findMovableBlockAround(range - 1, map[x + 1][y], fts, pokemon);
			}
			if (x > 0) {
				findMovableBlockAround(range - 1, map[x - 1][y], fts, pokemon);
			}
			if (y < this.sizeY) {
				findMovableBlockAround(range - 1, map[x][y + 1], fts, pokemon);
			}
			if (y < 0) {
				findMovableBlockAround(range - 1, map[x][y - 1], fts, pokemon);
			}
		}
		return fts;
	}

	public ArrayList<FightTerrain> findAttackableBlockAround(int range, FightTerrain ft, ArrayList<FightTerrain> fts) {
		if (range >= 0) {
			int x = ft.getX();
			int y = ft.getY();
			fts.add(map[x][y]);
			if (x < this.sizeX) {
				findAttackableBlockAround(range - 1, map[x + 1][y], fts);
			}
			if (x > 0) {
				findAttackableBlockAround(range - 1, map[x - 1][y], fts);
			}
			if (y < this.sizeY) {
				findAttackableBlockAround(range - 1, map[x][y + 1], fts);
			}
			if (y < 0) {
				findAttackableBlockAround(range - 1, map[x][y - 1], fts);
			}
		}
		return fts;
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

	public void draw() {
		for (FightTerrain[] fightTerrains : map) {
			for (FightTerrain fightTerrain : fightTerrains) {
				fightTerrain.draw();
			}
		}
	}

	public final int getSizeX() {
		return sizeX;
	}

	public final int getSizeY() {
		return sizeY;
	}

	public final ArrayList<Pokemon> getPokemonsOnMap() {
		return pokemonsOnMap;
	}

	public boolean addPokemonToMap(int x, int y, Pokemon pokemon) {
		if (!outOfMap(x, y) && pokemon != null && pokemon.getMoveType().check(map[y][x]) && this.getPokemonAt(x, y) == null) {
			// Can be Added!
			pokemon.setX(x);
			pokemon.setY(y);
			pokemonsOnMap.add(pokemon);
			return true;
		} else {
			return false;
		}
	}

}
