package utility.exception;

import logic_fight.FightPhase;

public class AbnormalPhaseOrderException extends Exception {
	private FightPhase nextPhase;
	public AbnormalPhaseOrderException(FightPhase nextPhase) {
		this.nextPhase = nextPhase;
	}
	
	public FightPhase getNextPhase() {
		return nextPhase;
	}

}
