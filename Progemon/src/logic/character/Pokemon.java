package logic.character;

import java.util.ArrayList;
import java.util.Comparator;

import graphic.DrawingUtility;
import graphic.IRenderable;
import logic.filters.Filter;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import logic.terrain.PathNode;
import utility.Pokedex;
import utility.RandomUtility;
import utility.StringUtility;

public class Pokemon implements Comparable<Pokemon>, Cloneable, IRenderable {

	private static final String DEFAULT_IMAGE_FILE_LOCATION = "load\\img\\pokemon";
	private double attackStat, defenceStat, speed, currentHP, nextTurnTime, fullHP;
	private int x, y, moveRange, attackRange, id;
	private Player owner;
	private MoveType moveType;
	private Element primaryElement, secondaryElement;
	private ArrayList<ActiveSkill> activeSkills = new ArrayList<ActiveSkill>();
	private ArrayList<PassiveSkill> passiveSkills = new ArrayList<PassiveSkill>();
	private String imageFileName;

	/** Used in findBlocksAround() */
	private FightMap currentFightMap = null;
	private FightTerrain currentFightTerrain = null;
	
	private int level = 1;
	private double exp = 0;

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
		fullHP = hp;
		this.id = id;
		this.attackStat = attackStat;
		this.defenceStat = defenceStat;
		this.moveRange = moveRange;
		this.speed = speed;
		this.currentHP = hp;
		this.moveType = moveType;
		nextTurnTime = 0;
		calculateNextTurnTime();
		setImageFileLocation();
	}

	public Pokemon(String[] args) {
		this.id = Integer.parseInt(args[0]);
		this.attackStat = Double.parseDouble(args[1]);
		this.defenceStat = Double.parseDouble(args[2]);
		this.speed = Double.parseDouble(args[3]);
		this.currentHP = Double.parseDouble(args[4]);
		fullHP = currentHP;
		this.moveRange = Integer.parseInt(args[5]);
		this.attackRange = Integer.parseInt(args[6]);
		this.moveType = toMoveType(args[7]);
		nextTurnTime = 0;
		calculateNextTurnTime();
		setImageFileLocation();
	}

	// move and attack
	public void move(int x, int y) {
		if (!currentFightMap.outOfMap(x, y)) {
			this.x = x;
			this.y = y;
			this.currentFightTerrain = currentFightMap.getFightTerrainAt(x, y);
		} else {
			this.x = -1;
			this.y = -1;
			this.currentFightTerrain = null;
		}
	}

	public void move(FightTerrain fightTerrain) {
		this.currentFightTerrain = fightTerrain;
		this.x = fightTerrain.getX();
		this.y = fightTerrain.getY();
	}

	public void attack(Pokemon p, int selectedSkill) {
		double damage = ((((0.4 * level + 2) * activeSkills.get(selectedSkill).getPower() * attackStat / defenceStat)
				* 0.2) + 2) * RandomUtility.randomPercent(85, 100);
		p.changeHP(-damage);
		// p.setHp(p.getHp() - activeSkills.get(selectedSkill).getPower() *
		// attackStat );
		System.out.println(this.getName() + " attacked " + p.getName() + " with "
				+ activeSkills.get(selectedSkill).getName() + "!");
		System.out.printf("%s's HP = %.2f  " + StringUtility.hpBar(p.getCurrentHP() / p.getFullHP()) + "\n",
				p.getName(), p.getCurrentHP());
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
		this.paths.clear();
		findBlocksAroundRecursive(range, new PathNode(this.getCurrentFightTerrain()),
				currentFightMap.getFightTerrainAt(x, y), currentFightMap, filter);
	}

	private ArrayList<PathNode> paths = new ArrayList<PathNode>();

	/** Called by findBlocksAround() method. */
	private void findBlocksAroundRecursive(int range, PathNode pathNode, FightTerrain fightTerrain, FightMap map,
			Filter filter) {
		if (filter.check(this, fightTerrain)) {
			paths.add(pathNode); // adds current pathNode
			if (range > 0) {
				// If range > 0, find blocks around it.
				int x = fightTerrain.getX();
				int y = fightTerrain.getY();
				FightTerrain nextFightTerrain;
				if (x < map.getSizeX() - 1) {
					nextFightTerrain = map.getMap()[y][x + 1];
					findBlocksAroundRecursive(range - 1, new PathNode(nextFightTerrain, pathNode), nextFightTerrain,
							map, filter);
				}
				if (x > 0) {
					nextFightTerrain = map.getMap()[y][x - 1];
					findBlocksAroundRecursive(range - 1, new PathNode(nextFightTerrain, pathNode), nextFightTerrain,
							map, filter);
				}
				if (y < map.getSizeY() - 1) {
					nextFightTerrain = map.getMap()[y + 1][x];
					findBlocksAroundRecursive(range - 1, new PathNode(nextFightTerrain, pathNode), nextFightTerrain,
							map, filter);
				}
				if (y > 0) {
					nextFightTerrain = map.getMap()[y - 1][x];
					findBlocksAroundRecursive(range - 1, new PathNode(nextFightTerrain, pathNode), nextFightTerrain,
							map, filter);
				}
			}

		}

	}

	public ArrayList<FightTerrain> getAvaliableFightTerrains() {
		ArrayList<FightTerrain> out = new ArrayList<FightTerrain>();
		for (PathNode pn : paths) {
			out.add(pn.getThisNode());
		}
		return out;
	}

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
	
	public void removeActiveSkill(ActiveSkill activeSkill){
		if(activeSkills.contains(activeSkill)){
			activeSkills.remove(activeSkill);
		}
	}
	
	public void removeActiveSkill(String activeSkillName){
		if(activeSkills.contains(ActiveSkill.getActiveSkill(activeSkillName))){
			activeSkills.remove(ActiveSkill.getActiveSkill(activeSkillName));
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

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		DrawingUtility.drawPokemon(this);
	}

	@Override
	public void getDepth() {
		// TODO Auto-generated method stub

	}

	// Getters and Setters

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

	public final ArrayList<ActiveSkill> getActiveSkills() {
		return activeSkills;
	}

	public final ArrayList<PassiveSkill> getPassiveSkills() {
		return passiveSkills;
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

	public final ArrayList<PathNode> getPaths() {
		return paths;
	}

	public final int getId() {
		return id;
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

	public int getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;

	}

	public final int getLevel() {
		return level;
	}

	public final double getCurrentHP() {
		return currentHP;
	}

	public final double getFullHP() {
		return fullHP;
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

	public final double getExp() {
		return exp;
	}
}
