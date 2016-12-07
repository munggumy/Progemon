package logic_fight.character;

import java.util.List;

import logic_fight.character.pokemon.Pokemon;

@FunctionalInterface
public interface PassiveSkillAction {
	public void apply(List<Pokemon> targets);
}
