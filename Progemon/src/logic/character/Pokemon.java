package logic.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import graphic.DrawingUtility;
import graphic.IRenderable;
import logic.filters.Filter;
import logic.filters.MoveFilter;
import logic.player.Player;
import logic.terrain.FightMap;
import logic.terrain.FightMap.Direction;
import logic.terrain.FightTerrain;
import logic.terrain.Path;
import utility.Pokedex;
import utility.RandomUtility;
import utility.StringUtility;

public class Pokemon implements Cloneable, IRenderable {

	// Constants and Variables

	private static final String DEFAULT_IMAGE_FILE_LOCATION = "load\\img\\pokemon";

	private double currentHP, nextTurnTime;
	private int moveRange, attackRange, id;
	private Player owner;
	private MoveType moveType;
	private Element primaryElement, secondaryElement;
	private ArrayList<ActiveSkill> activeSkills = new ArrayList<ActiveSkill>();
	private ArrayList<PassiveSkill> passiveSkills = new ArrayList<PassiveSkill>();
	private Stat base = new Stat();
	private Stat current = new Stat();
	private String imageFileName;
	/** Individual value */
	private double iv = RandomUtility.randomInt(0, 15) + 32;

	/**
	 * Holds avaliable paths to move generated by
	 * <code>findBlocksAround()</code>.
	 */
	private ArrayList<Path> paths = new ArrayList<Path>();
	private FightMap currentFightMap = null;
	private FightTerrain currentFightTerrain = null;

	private int level = 5;
	private double exp = 0;

	// Enums and inner classes

	/** This inner class is used to hold pokemon stats (base and current). */
	public class Stat implements Cloneable {
		public double attackStat, defenceStat, speed, fullHP;

		@Override
		public Object clone() throws CloneNotSupportedException {
			return (Stat) super.clone();
		}
	}

	/** Type of relocation on <code>fightMap</code>. */
	public static enum MoveType {
		FLY, SWIM, WALK;

		public boolean check(FightTerrain ft) {
			switch (ft.getType()) {
			case WATER:
				return this.equals(SWIM) || this.equals(FLY);
			case ROCK:
			case TREE:
				return this.equals(FLY);
			case GRASS:
			case GROUND:
			default:
				return true;
			}
		}
	}

	// Constructors

	public Pokemon(int id, double attackStat, double defenceStat, double speed, double hp, int moveRange,
			int attackRange, MoveType moveType) {
		this.id = id;
		this.base.attackStat = attackStat;
		this.base.defenceStat = defenceStat;
		this.moveRange = moveRange;
		this.attackRange = attackRange;
		this.base.speed = speed;
		this.base.fullHP = hp;
		this.moveType = moveType;
		nextTurnTime = 0;
		calculateCurrentStats();
		calculateNextTurnTime();
		resetHP();
		setImageFileLocation();
	}

	public Pokemon(int id, double attackStat, double defenceStat, double speed, double hp, int moveRange,
			int attackRange, MoveType moveType, Player owner) {
		this(id, attackStat, defenceStat, speed, hp, moveRange, attackRange, moveType);
		this.owner = owner;
		owner.addPokemon(this);
	}

	/**
	 * Load arguments to <code>Pokemon</code> by <code>String[]</code>. </br>
	 * Usage : id attack defence speed fullHP moveRange attackRange moveType.
	 * 
	 * @param args
	 *            String of arguments.
	 */
	public Pokemon(String[] args) {
		this.id = Integer.parseInt(args[0]);
		this.base.attackStat = Double.parseDouble(args[1]);
		this.base.defenceStat = Double.parseDouble(args[2]);
		this.base.speed = Double.parseDouble(args[3]);
		this.base.fullHP = Double.parseDouble(args[4]);
		this.moveRange = Integer.parseInt(args[5]);
		this.attackRange = Integer.parseInt(args[6]);
		this.moveType = toMoveType(args[7]);
		nextTurnTime = 0;
		calculateCurrentStats();
		calculateNextTurnTime();
		resetHP();
		setImageFileLocation();
	}

	// move and attack

	/**
	 * Use to relocate <code>this</code> on <code>fightMap</code>.
	 * <code>currentFightTerrain</code> will be automatically generated. Does
	 * not use <code>Filter.check()</code>.
	 * 
	 * @param x
	 *            position
	 * @param y
	 *            position
	 */
	public void move(int x, int y) {
		if (currentFightMap == null) {
			System.err.println("Pokemon.move() : [ID=" + id + "].currentFightMap not set");
		}
		if (!currentFightMap.outOfMap(x, y)) {
			this.currentFightTerrain = currentFightMap.getFightTerrainAt(x, y);
		} else {
			this.currentFightTerrain = null;
		}
	}

	public void move(FightTerrain fightTerrain) {
		this.currentFightTerrain = fightTerrain;
	}

	public void attack(Pokemon p, int selectedSkill) {
		double damage = ((((2 * level + 10) * activeSkills.get(selectedSkill).getPower() * current.attackStat
				/ p.current.defenceStat) * 0.004) + 2) * RandomUtility.randomPercent(85, 100);
		p.changeHP(-damage);
		// p.setHp(p.getHp() - activeSkills.get(selectedSkill).getPower() *
		// attackStat );
		System.out.println(this.getOwner().getName() + "'s " + this.getName() + " attacked " + p.getOwner().getName()
				+ "'s " + p.getName() + " with " + activeSkills.get(selectedSkill).getName() + "!" + " Damage dealt : "
				+ damage);
		System.out.printf("%s's HP = %.2f  " + StringUtility.hpBar(p.getCurrentHP() / p.getFullHP()) + "\n",
				p.getName(), p.getCurrentHP());
	}

	// Comparators

	public static Comparator<Pokemon> getSpeedComparator() {
		return new SpeedComparator();
	}

	private static class SpeedComparator implements Comparator<Pokemon> {
		@Override
		public int compare(Pokemon o1, Pokemon o2) {
			return Double.compare(o1.nextTurnTime, o2.nextTurnTime);
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

	public static Comparator<Pokemon> getHPComparator() {
		return new HPComparator();
	}

	private static class HPComparator implements Comparator<Pokemon> {
		@Override
		public int compare(Pokemon p1, Pokemon p2) {
			return Double.compare(p1.currentHP, p2.currentHP);
		}
	}

	// Clone

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (Pokemon) super.clone();
	}

	/**
	 * Generate <code>paths</code> from <code>range</code> and
	 * <code>filter</code>.
	 */
	// public void findBlocksAround(int range, Filter filter) {
	// this.paths.clear();
	// findBlocksAroundRecursive(range, new
	// PathNode(this.getCurrentFightTerrain()), this.getCurrentFightTerrain(),
	// currentFightMap, filter);
	// }

	public void findBlocksAround(int range, Filter filter) {
		paths.clear();
		findBlocksAroundRecursive(range, new Path(currentFightTerrain), currentFightTerrain, filter);
	}

	/** Called by findBlocksAround() method. */
	// private void findBlocksAroundRecursive(int range, PathNode currentPath,
	// FightTerrain fightTerrain, FightMap map,
	// Filter filter) {
	private void findBlocksAroundRecursive(int range, Path currentPath, FightTerrain currentFightTerrain,
			Filter filter) {
		if (filter.check(this, currentFightTerrain)) {
			paths.add(currentPath); // adds current pathNode
			if (range >= currentFightTerrain.getType().getMoveCost()) {
				// If range > currentMoveCost , find blocks around it.
				FightTerrain nextFightTerrain;
				for (Direction direction : Direction.values()) {
					nextFightTerrain = currentFightMap.getFightTerrainAt(currentFightTerrain, direction);
					if (nextFightTerrain != null) {
						findBlocksAroundRecursive(range - currentFightTerrain.getType().getMoveCost(),
								new Path(nextFightTerrain, currentPath), nextFightTerrain, filter);
					}
				}
			}
		}
	}

	public ArrayList<FightTerrain> getAvaliableFightTerrains() {
		ArrayList<FightTerrain> out = new ArrayList<FightTerrain>();
		FightTerrain last;
		for (Path path : paths) {
			last = path.getLast();
			System.out.println(last);
			if (last != null && !out.contains(last)) {
				out.add(last);
			}
		}
		return out;
	}

	public Path findPathTo(FightTerrain destination) {
		return findPathTo(destination, 20);
	}

	public Path findPathTo(FightTerrain destination, int limit) {

		/** Inner Class with counter */
		final class PathWithCounter extends Path {
			/**
			 * Generated Serial ID
			 */
			private static final long serialVersionUID = 7182452962943995194L;
			short totalCost = 0;

			PathWithCounter(FightTerrain nextFightTerrain, short counter) {
				super(nextFightTerrain);
				this.totalCost = counter;
			}

			PathWithCounter(FightTerrain nextFightTerrain, Path currentPath, short counter) {
				super(nextFightTerrain, currentPath);
				this.totalCost = counter;
			}
		}

		paths.clear();

		paths.add(new PathWithCounter(getCurrentFightTerrain(), (short) 0));

		boolean stupidPath;
		int roundsPassed = 0;
		short index = 0, lastPathIterCost = 0;
		int nextTerrainCost;
		Path lastPathIter;
		FightTerrain nextTerrain;
		Filter moveFilter = new MoveFilter();

		while (roundsPassed < limit) {
			if (index >= paths.size()) {
				System.err.println("Pokemon.findPath() : Failed to find Path");
				return null;
			}
			lastPathIter = paths.get(index);
			lastPathIterCost = ((PathWithCounter) lastPathIter).totalCost;
			for (Direction direction : Direction.values()) {
				nextTerrain = currentFightMap.getFightTerrainAt(lastPathIter.getLast(), direction);
				if (nextTerrain != null && moveFilter.check(this, nextTerrain)) {
					// nextTerrain is movable!
					nextTerrainCost = nextTerrain.getType().getMoveCost();
					stupidPath = false;
					for (Path otherPath : paths) {
						PathWithCounter otherPathWithCounter = (PathWithCounter) otherPath;
						if (otherPath.getLast().equals(nextTerrain)
								&& otherPathWithCounter.totalCost <= lastPathIterCost + nextTerrainCost) {
							// There is a duplicate element in paths.
							stupidPath = true;
							break;
						}
					}
					if (!stupidPath) {
						paths.add(new PathWithCounter(nextTerrain, lastPathIter,
								(short) (lastPathIterCost + nextTerrainCost)));
						if (nextTerrain.equals(destination)) {
							// We have found the solution!
							return paths.get(paths.size() - 1);
						}
					}
				}
			} // end for direction
			roundsPassed++;
			index++;
		} // end of while loop

		System.err.println("Pokemon.findPath() : Failed to find Path within internal limit.");
		return null;
	} // end of Pokemon.findPath()

	// ActiveSKill methods

	public void addActiveSkill(ActiveSkill newActiveSkill) {
		if (activeSkills.size() < 4) {
			activeSkills.add(newActiveSkill);
		}
	}

	public void addActiveSkill(String activeSkillName) {
		if (activeSkills.size() < 4) {
			activeSkills.add(ActiveSkill.getActiveSkill(activeSkillName));
		}
	}

	public void removeActiveSkill(ActiveSkill activeSkill) {
		if (activeSkills.contains(activeSkill)) {
			activeSkills.remove(activeSkill);
		}
	}

	public void removeActiveSkill(String activeSkillName) {
		if (activeSkills.contains(ActiveSkill.getActiveSkill(activeSkillName))) {
			activeSkills.remove(ActiveSkill.getActiveSkill(activeSkillName));
		}
	}

	// if (x < map.getSizeX() - 1) {
	// nextFightTerrain = map.getMap()[y][x + 1];
	// findBlocksAroundRecursive(range - 1, new Path(nextFightTerrain,
	// currentPath), nextFightTerrain, map,
	// filter);
	// }
	// if (x > 0) {
	// nextFightTerrain = map.getMap()[y][x - 1];
	// findBlocksAroundRecursive(range - 1, new Path(nextFightTerrain,
	// currentPath), nextFightTerrain, map,
	// filter);
	// }
	// if (y < map.getSizeY() - 1) {
	// nextFightTerrain = map.getMap()[y + 1][x];
	// findBlocksAroundRecursive(range - 1, new Path(nextFightTerrain,
	// currentPath), nextFightTerrain, map,
	// filter);
	// }
	// if (y > 0) {
	// nextFightTerrain = map.getMap()[y - 1][x];
	// findBlocksAroundRecursive(range - 1, new Path(nextFightTerrain,
	// currentPath), nextFightTerrain, map,
	// filter);
	// }

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
		nextTurnTime += (1 / current.speed);
	}

	public void calculateCurrentStats() {
		current.attackStat = calculateStatsEquation(base.attackStat);
		current.defenceStat = calculateStatsEquation(base.defenceStat);
		current.speed = calculateStatsEquation(base.speed);
		current.fullHP = (((2 * base.fullHP) + iv) * level * 0.01) + level + 10;
	}

	private double calculateStatsEquation(double inputBaseStat) {
		return (((2 * inputBaseStat) + iv) * level * 0.01) + 5;
	}

	public void sortPaths() {
		Collections.shuffle(paths);
		Collections.sort(paths);
		Collections.reverse(paths);
	}

	public static MoveType toMoveType(String moveTypeString) {
		for (MoveType mt : MoveType.values()) {
			if (mt.toString().equalsIgnoreCase(moveTypeString)) {
				return mt;
			}
		}
		return null;
	}

	// Graphics

	@Override
	public void draw() {
		DrawingUtility.drawPokemon(this);
	}

	@Override
	public void getDepth() {
	}

	// Getters and Setters

	public void resetHP() {
		currentHP = new Double(current.fullHP);
	}

	public double getHp() {
		return currentHP;
	}

	public void setHp(double hp) {
		if (Double.compare(hp, 0) < 0) {
			this.currentHP = 0;
		} else {
			this.currentHP = hp;
		}
	}

	public void changeHP(double change) {
		currentHP = currentHP + change < 0 ? 0 : currentHP + change;
	}

	public boolean isDead() {
		return Double.compare(currentHP, 0) <= 0;
	}

	public final int getID() {
		return id;
	}

	public String getName() {
		return Pokedex.getPokemonName(id);
	}

	public final Player getOwner() {
		return owner;
	}

	public final double getAttackStat() {
		return current.attackStat;
	}

	public final double getDefenceStat() {
		return current.defenceStat;
	}

	public final double getSpeed() {
		return current.speed;
	}

	public final double getNextTurnTime() {
		return nextTurnTime;
	}

	public final double getCurrentHP() {
		return currentHP;
	}

	public final double getFullHP() {
		return current.fullHP;
	}

	public final int getLevel() {
		return level;
	}

	public final void setLevel(int level) {
		this.level = level;
	}

	public final double getExp() {
		return exp;
	}

	public final Stat getBase() {
		return base;
	}

	public final Stat getCurrent() {
		return current;
	}

	public final MoveType getMoveType() {
		return moveType;
	}

	public final ArrayList<ActiveSkill> getActiveSkills() {
		return activeSkills;
	}

	public final ArrayList<PassiveSkill> getPassiveSkills() {
		return passiveSkills;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public final ArrayList<Path> getPaths() {
		return paths;
	}

	public final Element getPrimaryElement() {
		return primaryElement;
	}

	public final Element getSecondaryElement() {
		return secondaryElement;
	}

	public final FightMap getCurrentFightMap() {
		return currentFightMap;
	}

	public final void setCurrentFightMap(FightMap currentFightMap) {
		this.currentFightMap = currentFightMap;
	}

	public final int getMoveRange() {
		return moveRange;
	}

	public void setMoveRange(int moveRange) {
		this.moveRange = moveRange;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}

	public final FightTerrain getCurrentFightTerrain() {
		return currentFightTerrain;
	}

	public void setImageFileLocation(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public void setImageFileLocation() {
		setImageFileLocation(DEFAULT_IMAGE_FILE_LOCATION + "\\" + this.getName() + ".png");
	}

	public String getImageName() {
		return imageFileName;
	}
}
