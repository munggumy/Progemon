package logic_fight.character;

import java.util.List;

import logic_fight.character.pokemon.Pokemon;

@FunctionalInterface
public interface PassiveSkillAction {
	public static PassiveSkillAction heal(int amount) {
		return targets -> targets.forEach(pokemon -> pokemon.changeHP(amount));
	}

	public void apply(List<Pokemon> targets);
}
