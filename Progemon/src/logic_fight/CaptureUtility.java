package logic_fight;

import item.Pokeball;
import logic_fight.character.pokemon.Pokemon;
import utility.RandomUtility;

public class CaptureUtility {
	public static final int NUMBER_OF_SHAKES = 3;
	
	public boolean isCaptured(Pokemon captured, Pokeball pokeball) {
		double statusBonus;
		switch (captured.getStatus()) {
		case FREEZE:
			statusBonus = 2;
			break;
		case BURN:
		case PARALYZED:
		case POISON:
			statusBonus = 1.5;
		default:
			statusBonus = 1.0;
			break;
		}
		double modifiedCatchRate = (3 * captured.getFullHP() - 2 * captured.getCurrentHP()) * captured.getCatchRate()
				* pokeball.getCatchBonus() * statusBonus / (3 * captured.getFullHP());
		modifiedCatchRate = modifiedCatchRate >= 255 ? 255 : modifiedCatchRate;
		boolean out = true;
		for (int i = 1; i <= NUMBER_OF_SHAKES; i++){
			out = out && shake(modifiedCatchRate);
		}
		return out;
	}

	private boolean shake(double modifiedCatchRate) {
		double shakeProbability = 1048560 / Math.sqrt(Math.sqrt(16711680 / modifiedCatchRate));
		int randInt = RandomUtility.randomInt(0, 65535);
		return randInt < shakeProbability;
	}
}
