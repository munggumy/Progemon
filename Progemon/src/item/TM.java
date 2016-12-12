package item;

import logic_fight.character.activeSkill.ActiveSkill;

public class TM extends Item {

	private ActiveSkill activeSkill;

	public TM(ActiveSkill activeSkill) {
		super("TM: " + activeSkill.getName());
		this.activeSkill = activeSkill;
	}

	public ActiveSkill getActiveSkill() {
		return activeSkill;
	}

}
