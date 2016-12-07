package logic.character;

import utility.Phase;

public class PassiveSkill implements Comparable<PassiveSkill> {

	// Private Fields

	private String name;
	private Phase phaseToActivate;
	private PassiveSkillAction onActivate, onDeactivate;
	private int duration, power;
	private boolean alwaysActivate = false, activateOnce = false;

	// Constructor

	public PassiveSkill(String name, Phase phaseToActivate, int power) {
		this.name = name;
		this.phaseToActivate = phaseToActivate;
		this.power = power;
	}

	// Comparable Method

	@Override
	public int compareTo(PassiveSkill other) {
		return this.phaseToActivate.compareTo(other.phaseToActivate);
	}

	// Getters and Setters

	public final String getName() {
		return name;
	}

	public final Phase getPhaseToActivate() {
		return phaseToActivate;
	}

	public final PassiveSkillAction getOnActivate() {
		return onActivate;
	}

	public final PassiveSkillAction getOnDeactivate() {
		return onDeactivate;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final void setPhaseToActivate(Phase phaseToActivate) {
		this.phaseToActivate = phaseToActivate;
	}

	public final void setOnActivate(PassiveSkillAction onActivate) {
		this.onActivate = onActivate;
	}

	public final void setOnDeactivate(PassiveSkillAction onDeactivate) {
		this.onDeactivate = onDeactivate;
	}

	public final int getDuration() {
		return duration;
	}

	public final boolean isAlwaysActivate() {
		return alwaysActivate;
	}

	public final boolean isActivateOnce() {
		return activateOnce;
	}

	public final void setDuration(int duration) {
		this.duration = duration;
	}

	public final void setAlwaysActivate(boolean alwaysActivate) {
		this.alwaysActivate = alwaysActivate;
	}

	public final void setActivateOnce(boolean activateOnce) {
		this.activateOnce = activateOnce;
	}

}
