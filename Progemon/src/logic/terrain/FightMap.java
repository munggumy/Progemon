package logic.terrain;

import java.util.ArrayList;

import logic.character.Pokemon;

public class FightMap {
	
	public static final int SIZE_X = 8;
	public static final int SIZE_Y = 6;
	private static FightTerrain[][] map = new FightTerrain[SIZE_Y][SIZE_X];
	private static ArrayList<Pokemon> pokemonsOnMap = new ArrayList<Pokemon>();

	public static final FightTerrain[][] getMap() {
		return map;
	}

	public static final void setMap(FightTerrain[][] map) {
		FightMap.map = map;
	}
	
	public ArrayList<FightTerrain> findMovableBlockAround(int range, FightTerrain ft, ArrayList<FightTerrain> fts, Pokemon pokemon){
		if(range >= 0 && pokemon.getMoveType().check(ft)){
			int x = ft.getX();
			int y = ft.getY();
			fts.add(map[x][y]);
			if(x < SIZE_X){
				findMovableBlockAround(range - 1, map[x + 1][y], fts, pokemon);
			}
			if(x > 0){
				findMovableBlockAround(range - 1, map[x - 1][y], fts, pokemon);
			}
			if(y < SIZE_Y){
				findMovableBlockAround(range - 1, map[x][y + 1], fts, pokemon);
			}
			if(y < 0){
				findMovableBlockAround(range - 1, map[x][y - 1], fts, pokemon);
			}
		}
		return fts;
	}
	
	public ArrayList<FightTerrain> findAttackableBlockAround(int range, FightTerrain ft, ArrayList<FightTerrain> fts){
		if(range >= 0){
			int x = ft.getX();
			int y = ft.getY();
			fts.add(map[x][y]);
			if(x < SIZE_X){
				findAttackableBlockAround(range - 1, map[x + 1][y], fts);
			}
			if(x > 0){
				findAttackableBlockAround(range - 1, map[x - 1][y], fts);
			}
			if(y < SIZE_Y){
				findAttackableBlockAround(range - 1, map[x][y + 1], fts);
			}
			if(y < 0){
				findAttackableBlockAround(range - 1, map[x][y - 1], fts);
			}
		}
		return fts;
	}
	
	public Pokemon getPokemonAt(int x, int y){
		for (Pokemon pokemon : pokemonsOnMap) {
			if(pokemon.getX() == x && pokemon.getY() == y){
				return pokemon;
			}
		}
		return null;
	}
	
	public static boolean outOfMap(int x, int y){
		if(x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y){
			return true;
		} else {
			return false;
		}
	}
}
