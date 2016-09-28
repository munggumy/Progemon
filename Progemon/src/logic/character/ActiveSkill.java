package logic.character;

import java.util.ArrayList;
import java.util.Iterator;

public class ActiveSkill {
	private static final double DEFAULT_POWER = 50;
	private double power;
	private String name;
	private static ArrayList<ActiveSkill> allActiveSkills;

	private ActiveSkill(String skillName, double skillPower){
		name = skillName;
		power = skillPower;
		allActiveSkills.add(this);
	}
	
	public static ActiveSkill getActiveSkill(String skillName){
		Iterator<ActiveSkill> it = allActiveSkills.iterator();
		while (it.hasNext()) {
			ActiveSkill skill = (ActiveSkill) it.next();
			if(skill.name.equalsIgnoreCase(skillName)){
				return skill;
			}
		}
		return new ActiveSkill(skillName, DEFAULT_POWER);
	}

	public final double getPower() {
		return power;
	}

	public final String getName() {
		return name;
	}

	public final void setPower(double power) {
		this.power = power;
	}
	
	
	

}
