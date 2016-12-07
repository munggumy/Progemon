package logic.character;

import java.util.List;

@FunctionalInterface
public interface PassiveSkillAction {
	public void apply(List<Pokemon> targets);
}
