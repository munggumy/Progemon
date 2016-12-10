package logic_fight;

/** State in Finite State Machine */
public enum FightPhase {
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
	},
	preCapturePhase {
	},
	inputCapturePhase {
	},
	capturePhase {
	},
	postCapturePhase {
	};
	public FightPhase nextPhase() {
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
		case preCapturePhase:
			return inputCapturePhase;
		case inputCapturePhase:
			return capturePhase;
		case capturePhase:
			return postCapturePhase;
		case postCapturePhase:
			return endPhase;
		default:
			throw new IllegalArgumentException("(Phase.java:64) : Unknown Phase=" + this);
		}
	}
}
