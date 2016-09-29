package logic.character;

import java.util.ArrayList;
import java.util.Comparator;

import logic.filters.Filter;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import utility.Pokedex;

public class Pokemon implements Comparable<Pokemon>, Cloneable {

	private double attackStat, defenceStat, speed, hp, nextTurnTime;
	private int x, y, moveRange, attackRange, id;
	private Player owner;
	private MoveType moveType;
	private ElementType primaryElement, secondaryElement;
	private ArrayList<ActiveSkill> activeSkills = new ArrayList<ActiveSkill>();
	private ArrayList<PassiveSkill> passiveSkills = new ArrayList<PassiveSkill>();
	/** Used in findBlocksAround() */
	private ArrayList<FightTerrain> blocks = new ArrayList<FightTerrain>();
	private FightMap currentFightMap = null;
	private FightTerrain currentFightTerrain = null;

	public enum MoveType {
		FLY, SWIM, WALK;
	}

	public static enum ElementType {
		FIRE, GRASS, WATER, POISON, FLYING, BUG;
	}

	public static Comparator<Pokemon> getSpeedComparator() {
		return new SpeedComparator();
	}

	private static class SpeedComparator implements Comparator<Pokemon> {
		@Override
		public int compare(Pokemon o1, Pokemon o2) {
			if (Double.compare(o1.nextTurnTime, o2.nextTurnTime) > 0) {
				return 1;
			} else if (Double.compare(o1.nextTurnTime, o2.nextTurnTime) < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public static Comparator<Pokemon> getIDComparator() {
		return new IDComparator();
	}

	private static class IDComparator implements Comparator<Pokemon> {
		@Override
		public int compare(Pokemon o1, Pokemon o2) {
			return o1.id - o2.id;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (Pokemon) super.clone();
	}

	// Constructor

	public Pokemon(int id, double attackStat, double defenceStat, double speed, double hp, int moveRange,
			int attackRange, MoveType moveType, Player owner) {
		this(id, attackStat, defenceStat, speed, hp, moveRange, attackRange, moveType);
		this.owner = owner;

	}

	public Pokemon(int id, double attackStat, double defenceStat, double speed, double hp, int moveRange,
			int attackRange, MoveType moveType) {
		this.id = id;
		this.attackStat = attackStat;
		this.defenceStat = defenceStat;
		this.attackRange = attackRange;
		this.moveRange = moveRange;
		this.speed = speed;
		this.hp = hp;
		this.moveType = moveType;
		nextTurnTime = 0;
		calculateNextTurnTime();
	}

	/** Used in loadPokemon() */
	public Pokemon(String[] args) {
		this.id = Integer.parseInt(args[0]);
		this.attackStat = Double.parseDouble(args[1]);
		this.defenceStat = Double.parseDouble(args[2]);
		this.speed = Double.parseDouble(args[3]);
		this.hp = Double.parseDouble(args[4]);
		this.moveRange = Integer.parseInt(args[5]);
		this.attackRange = Integer.parseInt(args[6]);
		this.moveType = toMoveType(args[7]);
		nextTurnTime = 0;
		calculateNextTurnTime();
	}

	public void move(int x, int y) {
		this.x = x;
		this.y = y;
		this.currentFightTerrain = currentFightMap.getFightTerrainAt(x, y);
	}

	public void move(FightTerrain fightTerrain) {
		this.currentFightTerrain = fightTerrain;
		this.x = fightTerrain.getX();
		this.y = fightTerrain.getY();
	}

	public void attack(Pokemon p, int selectedSkill) {
		p.setHp(p.getHp() - activeSkills.get(selectedSkill).getPower() * attackStat);
		System.out.println(this.getName() + " attacked " + p.getName() + " with "
				+ activeSkills.get(selectedSkill).getName() + " !");
	}

	/** Compares nextTurnTime. Used for sort in turn */
	@Override
	public int compareTo(Pokemon other) {
		if (Double.compare(this.nextTurnTime, other.nextTurnTime) > 0) {
			return 1;
		} else if (Double.compare(this.nextTurnTime, other.nextTurnTime) < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * Created to clear blocks before running findBlocksAroundRecursive()
	 * method.
	 */
	public void findBlocksAround(int range, Filter filter) {
		this.blocks.clear();
		findBlocksAroundRecursive(range, currentFightMap.getFightTerrainAt(x, y), currentFightMap, filter);
	}

	/** Called by findBlocksAround() method. */
	private void findBlocksAroundRecursive(int range, FightTerrain fightTerrain, FightMap map, Filter filter) {
		if (filter.check(this, fightTerrain)) {
			this.blocks.add(fightTerrain);
			if (range > 0) {
				// If range > 0, find blocks around it.
				int x = fightTerrain.getX();
				int y = fightTerrain.getY();
				if (x < map.getSizeX() - 1) {
					findBlocksAroundRecursive(range - 1, map.getMap()[y][x + 1], map, filter);
				}
				if (x > 0) {
					findBlocksAroundRecursive(range - 1, map.getMap()[y][x - 1], map, filter);
				}
				if (y < map.getSizeY() - 1) {
					findBlocksAroundRecursive(range - 1, map.getMap()[y + 1][x], map, filter);
				}
				if (y < 0) {
					findBlocksAroundRecursive(range - 1, map.getMap()[y - 1][x], map, filter);
				}
			}
		}

	}

	public void addActiveSkill(ActiveSkill newActiveSkill) {
		if (activeSkills.size() < 4) {
			activeSkills.add(newActiveSkill);
		}
	}

	// public ArrayList<FightTerrain> findMovableBlockAround(int range,
	// FightTerrain ft, ArrayList<FightTerrain> fts,
	// Pokemon pokemon) {
	// if (range >= 0 && pokemon.getMoveType().check(ft)) {
	// int x = ft.getX();
	// int y = ft.getY();
	// fts.add(map[y][x]);
	// if (x < this.sizeX - 1) {
	// findMovableBlockAround(range - 1, map[y][x + 1], fts, pokemon);
	// }
	// if (x > 0) {
	// findMovableBlockAround(range - 1, map[y][x - 1], fts, pokemon);
	// }
	// if (y < this.sizeY - 1) {
	// findMovableBlockAround(range - 1, map[y + 1][x], fts, pokemon);
	// }
	// if (y < 0) {
	// findMovableBlockAround(range - 1, map[y - 1][x], fts, pokemon);
	// }
	// }
	// return fts;
	// }
	//
	// public ArrayList<FightTerrain> findAttackableBlockAround(int range,
	// FightTerrain ft, ArrayList<FightTerrain> fts) {
	// if (range >= 0) {
	// int x = ft.getX();
	// int y = ft.getY();
	// fts.add(map[y][x]);
	// if (x < this.sizeX - 1) {
	// findAttackableBlockAround(range - 1, map[y][x + 1], fts);
	// }
	// if (x > 0) {
	// findAttackableBlockAround(range - 1, map[y][x - 1], fts);
	// }
	// if (y < this.sizeY - 1) {
	// findAttackableBlockAround(range - 1, map[y + 1][x], fts);
	// }
	// if (y < 0) {
	// findAttackableBlockAround(range - 1, map[y - 1][x], fts);
	// }
	// }
	// return fts;
	// }

	public void calculateNextTurnTime() {
		nextTurnTime += (1 / this.speed);
	}

	public static MoveType toMoveType(String moveTypeString) {
		for (MoveType mt : MoveType.values()) {
			if (mt.toString().equalsIgnoreCase(moveTypeString)) {
				return mt;
			}
		}
		return null;
	}

	// Getters and Setters

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		if (Double.compare(hp, 0) < 0) {
			this.hp = 0;
		} else {
			this.hp = hp;
		}
	}

	public boolean isDead() {
		return Double.compare(hp, 0) <= 0;
	}

	public final int getMoveRange() {
		return moveRange;
	}

	public final double getSpeed() {
		return speed;
	}

	public final double getNextTurnTime() {
		return nextTurnTime;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final int getID() {
		return id;
	}

	public final Player getOwner() {
		return owner;
	}

	public final MoveType getMoveType() {
		return moveType;
	}

	public final FightTerrain getCurrentFightTerrain() {
		return currentFightTerrain;
	}

	public final ArrayList<ActiveSkill> getActiveSkills() {
		return activeSkills;
	}

	public final ArrayList<PassiveSkill> getPassiveSkills() {
		return passiveSkills;
	}

	public final void setX(int x) {
		this.x = x;
	}

	public final void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return Pokedex.getPokemonName(id);
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public final double getAttackStat() {
		return attackStat;
	}

	public final double getDefenceStat() {
		return defenceStat;
	}

	public final int getId() {
		return id;
	}

	public final ElementType getPrimaryElement() {
		return primaryElement;
	}

	public final ElementType getSecondaryElement() {
		return secondaryElement;
	}

	public final ArrayList<FightTerrain> getBlocks() {
		return blocks;
	}

	public final FightMap getCurrentFightMap() {
		return currentFightMap;
	}

	public final void setCurrentFightMap(FightMap currentFightMap) {
		this.currentFightMap = currentFightMap;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}
}
