package logic.character;

import java.util.ArrayList;
import java.util.Iterator;

import utility.StringUtility;

public class ActiveSkill {
	private static final double DEFAULT_POWER = 50;
	private double power;
	private String name;
	// TODO type of active skill?
	private static ArrayList<ActiveSkill> allActiveSkills = new ArrayList<ActiveSkill>();

	private ActiveSkill(String skillName, double skillPower) {
		setName(skillName);
		setPower(skillPower);
		allActiveSkills.add(this);
	}

	public static ActiveSkill getActiveSkill(String skillName) {
		return getActiveSkill(skillName, DEFAULT_POWER);
	}

	public static ActiveSkill getActiveSkill(String skillName, double powerIfNotCreated) {
		Iterator<ActiveSkill> it = allActiveSkills.iterator();
		while (it.hasNext()) {
			ActiveSkill skill = (ActiveSkill) it.next();
			if (skill.name.equalsIgnoreCase(skillName)) {
				return skill;
			}
		}
		return new ActiveSkill(skillName, powerIfNotCreated);
	}

	// Array Getter
	public static ArrayList<ActiveSkill> getAllActiveSkills() {
		return allActiveSkills;
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
		if (name == null) {
			name = "";
		} else {
			name = StringUtility.toTitleCase(name);
		}
		this.name = name;
	}

}
