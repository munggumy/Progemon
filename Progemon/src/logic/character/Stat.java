package logic.character;

import utility.RandomUtility;
import utility.exception.InvalidStatStage;

/**
 * Class for each Stat[attack, defense, speed] <br>
 * Base : pokemon factor <br>
 * IV : random factor <br>
 * EV : item factor <br>
 * stage: battle factor
 * 
 */
class Stat {
	public static final int STAGE_MAX = 6, STAGE_MIN = -6, EV_MAX = 252, EV_MIN = 0;
	protected int base, iv, ev, stage;
	public int current;

	Stat(int base, int level) {
		this.base = base;
		iv = RandomUtility.randomInt(0, 31);
		ev = 0;
		resetStage();
		calculateCurrent(level);
	}

	@Override
	public String toString() {
		return "[current=" + current + ",base=" + base + ",iv=" + iv + ",ev=" + ev + ",stage=" + stage + "]";
	}

	/** Calculated after level up */
	void calculateCurrent(int level) {
		current = Math.floorDiv(((2 * base + iv + Math.floorDiv(ev, 4)) * level), 100) + 5;
	}

	void changeStage(int change) throws InvalidStatStage {
		stage += change;
		if (stage < STAGE_MIN) {
			throw new InvalidStatStage(InvalidStatStage.LOW);
		} else if (stage > STAGE_MAX) {
			throw new InvalidStatStage(InvalidStatStage.HIGH);
		}
	}

	void resetStage() {
		stage = 0;
	}

	void changeEV(int change) {
		ev += change;
		if (ev > EV_MAX || ev < EV_MIN) {
			ev -= change;
			System.err.println("EV not changed");
		}
	}
}
