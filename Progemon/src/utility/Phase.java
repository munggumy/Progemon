package utility;

/** State in Finite State Machine */
public enum Phase {
	initialPhase {
	},
	preMovePhase {
	},
	inputMovePhase {
	},
	movePhase {
	},
	postMovePhase {
	},
	preAttackPhase {
	},
	inputAttackPhase {
	},
	attackPhase {
	},
	postAttackPhase {
	},
	endPhase {
	};
	public Phase nextPhase() {
		switch (this) {
		case initialPhase:
			return preMovePhase;
		case preMovePhase:
			return inputMovePhase;
		case inputMovePhase:
			return movePhase;
		case movePhase:
			return postMovePhase;
		case postMovePhase:
			return preAttackPhase;
		case preAttackPhase:
			return inputAttackPhase;
		case inputAttackPhase:
			return attackPhase;
		case attackPhase:
			return postAttackPhase;
		case postAttackPhase:
			return endPhase;
		case endPhase:
			return initialPhase;
		default:
			System.err.println("Phase.java : Unknown Phase");
			return null;
		}
	}
}
