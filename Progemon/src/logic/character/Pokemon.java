package logic.character;

import java.util.ArrayList;
import java.util.Comparator;

import logic.FightTerrainFilter;
import logic.terrain.FightTerrain;

public class Pokemon implements Comparable<Pokemon> {

	private double attack, defend, moveRange, speed, hp, nextTurnTime;
	private int x, y , id;
	private Player owner;
	private MoveType moveType;
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
			default:
				return true;
			}
		}
	}

	public static Comparator<Pokemon> getSpeedComparator(){
		return new SpeedComparator();
	}
	
	private static class SpeedComparator implements Comparator<Pokemon>{
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
	
	public static Comparator<Pokemon> getIDComparator(){
		return new IDComparator();
	}
	
	private static class IDComparator implements Comparator<Pokemon>{
		@Override
		public int compare(Pokemon o1, Pokemon o2) {
			return o1.id - o2.id;
		}
	}
	
	public Pokemon(double attack, double defend, double moveRange, double speed, double hp, int x, int y,
			MoveType moveType) {
		this.attack = attack;
		this.defend = defend;
		this.moveRange = moveRange;
		this.speed = speed;
		this.hp = hp;
		this.x = x;
		this.y = y;
		nextTurnTime = 1 / speed;
		this.moveType = moveType;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		if (hp < 0) {
			hp = 0;
		} else {
			this.hp = hp;
		}
	}

	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void attack(Pokemon p, int selectedSkill) {
		p.setHp(p.getHp() - activeSkills.get(selectedSkill).getPower() * attack);
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

	public final double getAttack() {
		return attack;
	}

	public final double getDefend() {
		return defend;
	}

	public final double getMoveRange() {
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

}
