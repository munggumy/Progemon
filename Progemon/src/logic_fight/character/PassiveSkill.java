package logic_fight.character;

import java.util.List;

import logic_fight.FightPhase;
import logic_fight.character.pokemon.Pokemon;
//TODO PASSIVESKILL!
public class PassiveSkill implements Comparable<PassiveSkill> {

	// Private Fields

	private String name;
	private FightPhase phaseToActivate;
	private PassiveSkillAction onActivate, onDeactivate;
	private int duration, power;
	private boolean alwaysActivate = false, activateOnce = false;

	// Constructor

	public PassiveSkill(String name, FightPhase phaseToActivate, int power) {
		this.name = name;
		this.phaseToActivate = phaseToActivate;
		this.power = power;
	}

	// Comparable Method

	/** Compares by Phase */
	@Override
	public int compareTo(PassiveSkill other) {
		return this.phaseToActivate.compareTo(other.phaseToActivate);
	}

	// Getters and Setters

	public final String getName() {
		return name;
	}

	public final FightPhase getPhaseToActivate() {
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

	public final void setPhaseToActivate(FightPhase phaseToActivate) {
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

	public final int getPower() {
		return power;
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

	public final void setPower(int power) {
		this.power = power;
	}
	
	public void activate(List<Pokemon> targets){
		onActivate.apply(targets);
	}

	// templates

	public static PassiveSkill burn = new PassiveSkill("Burn", FightPhase.initialPhase, 20);
	static {
		burn.setActivateOnce(false);
		burn.setAlwaysActivate(true);
		burn.setOnActivate(PassiveSkillAction.damage(burn.power));
	}

}
