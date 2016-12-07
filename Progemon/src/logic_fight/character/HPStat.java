package logic_fight.character;

import utility.exception.InvalidStatStage;

public class HPStat extends Stat {

	public HPStat(int hp, int level) {
		super(hp, level);
	}
	
	@Override
	public void calculateCurrent(int level) {
		current = Math.floorDiv(((2 * base + iv + Math.floorDiv(ev, 4)) * level), 100) + level + 10;
	}

	@Override
	public void changeStage(int change) throws InvalidStatStage {
	}

	@Override
	public void resetStage() {
		stage = 0;
	}
	
	public int getFull(){
		return base;
	}

}
