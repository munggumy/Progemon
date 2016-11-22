package logic.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import graphic.Animation;
import graphic.DrawingUtility;
import graphic.IRenderable;
import logic.terrain.FightTerrain;
import utility.StringUtility;

public class ActiveSkill extends Animation implements IRenderable {
	private static final double DEFAULT_POWER = 50;
	private double power;
	private String name;
	// TODO type of active skill?
	private static ArrayList<ActiveSkill> allActiveSkills = new ArrayList<ActiveSkill>();
	private FightTerrain attackTerrain, targetTerrain;

	private ActiveSkill(String skillName, double skillPower) {
		super();
		setName(skillName);
		setPower(skillPower);
		allActiveSkills.add(this);
		loadImage("load/img/skill/Flamethrower/all.png");
	}

	@Override
	public String toString() {
		return "ActiveSkill " + name + " POWER:" + power;
	}

	public static ActiveSkill getActiveSkill(String skillName) {
		return getActiveSkill(skillName, DEFAULT_POWER, true);
	}

	public static ActiveSkill getActiveSkill(String skillName, double powerIfNotCreated) {
		return getActiveSkill(skillName, powerIfNotCreated, true);
	}

	public static ActiveSkill getActiveSkill(String skillName, double powerIfNotCreated, boolean verbose) {
		Iterator<ActiveSkill> it = allActiveSkills.iterator();
		while (it.hasNext()) {
			ActiveSkill skill = (ActiveSkill) it.next();
			if (skill.name.equalsIgnoreCase(skillName)) {
				return skill;
			}
		}
		if (verbose) {
			System.out.println("ActiveSkill : ActiveSkill " + skillName + " not found...");
			System.out.println("ActiveSkill : Creating new ActiveSkill with power " + powerIfNotCreated + ".");
		}
		return new ActiveSkill(skillName, powerIfNotCreated);
	}

	// Array Getter
	public static List<ActiveSkill> getAllActiveSkills() {
		return Collections.unmodifiableList(allActiveSkills);
	}

	public static void clearAllActiveSkills() {
		allActiveSkills.clear();
	}

	// getters and setters
	public final double getPower() {
		return power;
	}

	public final String getName() {
		return name;
	}

	public final void setPower(double power) {
		this.power = power < 0 ? 0 : power;
	}

	private final void setName(String name) {
		if (name == null || name.matches("\\w") || name.length() <= 1) {
			name = "";
			System.err.println("ActiveSkill : Skill has no name!");
		} else {
			name = StringUtility.toTitleCase(name);
		}
		this.name = name;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		DrawingUtility.drawSkill(this);
	}

	@Override
	public void getDepth() {
		// TODO Auto-generated method stub

	}

	public void setAttackTerrain(FightTerrain attackTerrain) {
		this.attackTerrain = attackTerrain;
	}

	public void setTargetTerrain(FightTerrain targetTerrain) {
		this.targetTerrain = targetTerrain;
	}

	public FightTerrain getAttackTerrain() {
		return attackTerrain;
	}

	public FightTerrain getTargetTerrain() {
		return targetTerrain;
	}

}
