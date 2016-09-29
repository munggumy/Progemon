package logic.character;

import java.util.ArrayList;
import java.util.Comparator;

import graphic.DrawingUtility;
import graphic.IRenderable;
import logic.FightTerrainFilter;
import logic.terrain.FightTerrain;
import utility.Pokedex;

public class Pokemon implements Comparable<Pokemon>, Cloneable, IRenderable {

	private double attackStat, defenceStat, speed, hp, nextTurnTime;
	private int x, y, moveRange, id;
	private Player owner;
	private MoveType moveType;
	private ElementType primaryElement, secondaryElement;
	private ArrayList<ActiveSkill> activeSkills = new ArrayList<ActiveSkill>();
	private ArrayList<PassiveSkill> passiveSkills = new ArrayList<PassiveSkill>();
	

	public static enum MoveType implements FightTerrainFilter {
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
		return (Pokemon)super.clone();
	}

	// Constructor

	public Pokemon(int id, double attackStat, double defenceStat, int moveRange, double speed, double hp,
			MoveType moveType, Player owner){
		this(id, attackStat, defenceStat, moveRange, speed, hp, moveType);
		this.owner = owner;
		
	}

	public Pokemon(int id, double attackStat, double defenceStat, int moveRange, double speed, double hp,
			MoveType moveType) {
		this.id = id;
		this.attackStat = attackStat;
		this.defenceStat = defenceStat;
		this.moveRange = moveRange;
		this.speed = speed;
		this.hp = hp;
		this.x = x;
		this.y = y;
		this.moveType = moveType;
		nextTurnTime = 0;
		calculateNextTurnTime();
	}

	public Pokemon(String[] args) {
		this.id = Integer.parseInt(args[0]);
		this.attackStat = Double.parseDouble(args[1]);
		this.defenceStat = Double.parseDouble(args[2]);
		this.moveRange = Integer.parseInt(args[3]);
		this.speed = Double.parseDouble(args[4]);
		this.hp = Double.parseDouble(args[5]);
		this.moveType = toMoveType(args[6]);
		nextTurnTime = 0;
		calculateNextTurnTime();
	}

	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void attack(Pokemon p, int selectedSkill) {
		p.setHp(p.getHp() - activeSkills.get(selectedSkill).getPower() * attackStat);
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
		return hp;
	}

	public void setHp(double hp) {
		if (Double.compare(hp, 0) < 0) {
			hp = 0;
		} else {
			this.hp = hp;
		}
	}

	public boolean isDead() {
		return Double.compare(hp, 0) <= 0;
	}

	public final double getAttack() {
		return attackStat;
	}

	public final double getDefend() {
		return defenceStat;
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

	public final void setX(int x) {
		this.x = x;
	}

	public final void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return Pokedex.getPokemonName(id);
	}
	
	public void setOwner(Player owner){
		this.owner = owner;
	}
	
	public String getImageName(){
		return "load\\img\\pokemon\\" + getName() + ".png";
	}
}
