package logic.character;

import utility.exception.InvalidStatStage;

public class HPStat extends Stat {

	HPStat(int hp, int level) {
		super(hp, level);
	}
	
	@Override
	void calculateCurrent(int level) {
		current = Math.floorDiv(((2 * base + iv + Math.floorDiv(ev, 4)) * level), 100) + level + 10;
	}

	@Override
	void changeStage(int change) throws InvalidStatStage {
	}

	@Override
	void resetStage() {
		stage = 0;
	}
	
	int getFull(){
		return base;
	}

}
