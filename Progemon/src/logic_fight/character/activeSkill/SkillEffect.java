package logic_fight.character.activeSkill;

import java.util.Objects;

import org.omg.PortableInterceptor.SUCCESSFUL;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.character.pokemon.NonVolatileStatus;
import utility.RandomUtility;
import utility.StringUtility;
import utility.exception.InvalidStatStage;

/** For logical effects done by skill */
@FunctionalInterface
public interface SkillEffect {

	/** Use for QUICK ATTACK, AQUA JET*/
	public static final SkillEffect quick = (arg1, arg2, arg3) -> {
		arg2.changeNextTurnTime(-arg2.getSpeed() / 2);
		System.out.println(arg2.getName() + " is quick!");
		return arg1;
	};

	public static final SkillEffect normal = (arg1, arg2, arg3) -> {
		return arg1;
	};

	public static SkillEffect status(NonVolatileStatus status) {
		return (arg1, arg2, arg3) -> {
			arg3.setStatus(status);
			return arg1;
		};
	}
	
	public static SkillEffect thisStatChange(String statName, int stageChange){
		return (arg1, arg2, arg3) -> {
			try {
				arg2.getStats().get(statName.toLowerCase()).changeStage(stageChange);
			} catch (InvalidStatStage e) {
				if(e.type == InvalidStatStage.HIGH){
					System.out.println(arg2.getName() + "'s " + StringUtility.toTitleCase(statName) + " won't go any higher!");
				} else if (e.type == InvalidStatStage.LOW){
					System.out.println(arg2.getName() + "'s " + StringUtility.toTitleCase(statName) + " won't go any lower!");
				}
			}
			return arg1;
		};
	}
	
	public static SkillEffect otherStatChange(String statName, int stageChange){
		return (arg1, arg2, arg3) -> {
			try {
				arg3.getStats().get(statName.toLowerCase()).changeStage(stageChange);
			} catch (InvalidStatStage e) {
				if(e.type == InvalidStatStage.HIGH){
					System.out.println(arg3.getName() + "'s " + StringUtility.toTitleCase(statName) + " won't go any higher!");
				} else if (e.type == InvalidStatStage.LOW){
					System.out.println(arg3.getName() + "'s " + StringUtility.toTitleCase(statName) + " won't go any lower!");
				}
			}
			return arg1;
		};
	}

	public static SkillEffect modifyChance(SkillEffect arg1, int percentChance) {
		if (percentChance < 0 && percentChance > 100){
			throw new IllegalArgumentException("Invalid percentChance=" + percentChance);
		}
		if (RandomUtility.randomInt(1, 100) <= percentChance) {
			return arg1;
		} else {
			return normal;
		}
	}

	/** Must return itself for chaining */
	ActiveSkill apply(ActiveSkill activeSkill, Pokemon ours, Pokemon theirs);

	/** Use this to chain SkillEffect's */
	default SkillEffect andThen(SkillEffect nextSkillEffect) {
		Objects.requireNonNull(nextSkillEffect);
		return (arg1, arg2, arg3) -> {
			return nextSkillEffect.apply(this.apply(arg1, arg2, arg3), arg2, arg3);
		};
	}
}
